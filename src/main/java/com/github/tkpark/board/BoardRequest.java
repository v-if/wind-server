package com.github.tkpark.board;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

public class BoardRequest {

    @NotBlank(message = "boardTp must be provided")
    private String boardTp;

    @NotBlank(message = "name must be provided")
    private String name;

    //@NotBlank(message = "phone must be provided")
    private String phone;

    @NotBlank(message = "email must be provided")
    private String email;

    @NotBlank(message = "content must be provided")
    private String content;

    protected BoardRequest() {/*empty*/}

    public BoardRequest(String boardTp, String name, String phone, String email, String content) {
        this.boardTp = boardTp;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.content = content;
    }

    public String getBoardTp() {
        return boardTp;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("boardTp", boardTp)
                .append("name", name)
                .append("phone", phone)
                .append("email", email)
                .append("content", content)
                .toString();
    }

}