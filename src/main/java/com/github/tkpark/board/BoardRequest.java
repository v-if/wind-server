package com.github.tkpark.board;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

public class BoardRequest {

    @NotBlank(message = "피드백 종류를 선택해주세요.")
    private String boardTp;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    //@NotBlank(message = "휴대폰을 입력해주세요.")
    private String phone;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String email;

    @NotBlank(message = "내용을 입력해주세요.")
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