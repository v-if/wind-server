package com.github.tkpark.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @Column(name = "[board_id]")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "[board_tp]")
    private String boardTp;

    @Column(name = "[name]")
    private String name;

    @Column(name = "[phone]")
    private String phone;

    @Column(name = "[email]")
    private String email;

    @Column(name = "[content]")
    private String content;

    @Column(name = "[create_date]")
    private LocalDateTime createDate;

    public Board(String boardTp, String name, String phone, String email, String content) {
        this(null, boardTp, name, phone, email, content, null);
    }

    public Board(Long boardId, String boardTp, String name, String phone, String email, String content, LocalDateTime createDate) {
        checkArgument(isNotEmpty(name), "name must be provided");
        checkArgument(
                name.length() >= 1 && name.length() <= 50,
                "name length must be between 1 and 50 characters"
        );
        checkArgument(
                isEmpty(content) || content.length() <= 1000,
                "content length must be less than 1000 characters"
        );

        this.boardId = boardId;
        this.boardTp = boardTp;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.content = content;
        this.createDate = defaultIfNull(createDate, now());
    }

    static public class Builder {
        private Long boardId;
        private String boardTp;
        private String name;
        private String phone;
        private String email;
        private String content;
        private LocalDateTime createDate;

        public Builder() {/*empty*/}

        public Builder(Board board) {
            this.boardId = board.boardId;
            this.boardTp = board.boardTp;
            this.name = board.name;
            this.phone = board.phone;
            this.email = board.email;
            this.content = board.content;
            this.createDate = board.createDate;
        }

        public Builder seq(Long boardId) {
            this.boardId = boardId;
            return this;
        }

        public Builder boardTp(String boardTp) {
            this.boardTp = boardTp;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createAt(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Board build() {
            return new Board(
                    boardId,
                    boardTp,
                    name,
                    phone,
                    email,
                    content,
                    createDate
            );
        }
    }

}