package com.github.tkpark.users;

import static com.github.tkpark.utils.ApiUtils.success;

import com.github.tkpark.errors.NotFoundException;
import com.github.tkpark.errors.UnauthorizedException;
import com.github.tkpark.security.Jwt;
import com.github.tkpark.security.JwtAuthentication;
import com.github.tkpark.security.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.tkpark.utils.ApiUtils.ApiResult;

@Slf4j
@RestController
@RequestMapping("api/users")
public class UserRestController {

    private final Jwt jwt;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public UserRestController(Jwt jwt, AuthenticationManager authenticationManager, UserService userService) {
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(path = "login")
    public ApiResult<LoginResult> login(
            @Valid @RequestBody LoginRequest request
    ) throws UnauthorizedException {
        log.info("========= /login =========");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials())
            );
            final User user = (User) authentication.getDetails();
            final String token = user.newJwt(
                    jwt,
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
            );
            return success(new LoginResult(token, user));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @PostMapping(path = "signup")
    public ApiResult<SignupResult> signup(
            @Valid @RequestBody SignupRequest request
    ) throws UnauthorizedException {
        log.info("========= /signup =========");
        try {
            return success(userService.signup(request.getName(), request.getEmail(), request.getPasswd()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @GetMapping(path = "me")
    public ApiResult<UserDto> me(
            // JwtAuthenticationTokenFilter 에서 JWT 값을 통해 사용자를 인증한다.
            // 사용자 인증이 정상으로 완료됐다면 @AuthenticationPrincipal 어노테이션을 사용하여 인증된 사용자 정보(JwtAuthentication)에 접근할 수 있다.
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        log.info("========= /me =========");
        return success(
                userService.findById(authentication.id)
                        .map(UserDto::new)
                        .orElseThrow(() -> new NotFoundException("Could nof found user for " + authentication.id))
        );
    }

}