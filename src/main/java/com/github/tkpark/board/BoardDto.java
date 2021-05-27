package com.github.tkpark.board;

import lombok.Data;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class BoardDto {

    private Long boardId;

    private String boardTp;

    private String name;

    private String phone;

    private String email;

    private String content;

    private LocalDateTime createDate;

    public BoardDto(Board source) {
        copyProperties(source, this);
    }

}