package com.github.tkpark.users;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

public class SignupRequest {

    @NotBlank(message = "name must be provided")
    private String name;

    @NotBlank(message = "email must be provided")
    private String email;

    @NotBlank(message = "passwd must be provided")
    private String passwd;

    protected SignupRequest() {/*empty*/}

    public SignupRequest(String name, String email, String passwd) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("email", email)
                .append("passwd", passwd)
                .toString();
    }

}