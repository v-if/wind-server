package com.github.tkpark.users;

import com.github.tkpark.security.Jwt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "[seq]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "[name]")
    private String name;

    @Column(name = "[email]")
    private String email;

    @Column(name = "[passwd]")
    private String password;

    @Column(name = "[login_count]")
    private int loginCount;

    @Column(name = "[last_login_at]")
    private LocalDateTime lastLoginAt;

    @Column(name = "[create_at]")
    private LocalDateTime createAt;

    public User(String name, String email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    public User(Long seq, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 10,
                "name length must be between 1 and 10 characters"
        );
        checkNotNull(email, "email must be provided");
        checkNotNull(password, "password must be provided");

        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public String newJwt(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(seq, name, roles);
        return jwt.create(claims);
    }

    public void login(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password)) {
            throw new IllegalArgumentException("Bad credential");
        }
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(seq, user.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("email", email)
                .append("password", "[PROTECTED]")
                .append("loginCount", loginCount)
                .append("lastLoginAt", lastLoginAt)
                .append("createAt", createAt)
                .toString();
    }

}