package com.github.tkpark.board;

import com.github.tkpark.errors.NotFoundException;
import com.github.tkpark.errors.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.tkpark.utils.ApiUtils.ApiResult;
import static com.github.tkpark.utils.ApiUtils.success;

@Slf4j
@RestController
@RequestMapping("api/board")
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(path = "write")
    public ApiResult<BoardResult> write(
            @Valid @RequestBody BoardRequest request
    ) throws UnauthorizedException {
        log.info("========= /write =========");
        try {
            return success(boardService.write(request.getBoardTp(), request.getName(), request.getPhone(), request.getEmail(), request.getContent()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @GetMapping(path = "{id}")
    public ApiResult<BoardDto> findById(@PathVariable Long id) {
        return success(
                boardService.findById(id)
                        .map(BoardDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found board for " + id))
        );
    }

}