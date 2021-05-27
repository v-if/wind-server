package com.github.tkpark.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public BoardResult write(String boardTp, String name, String phone, String email, String content) {
        checkNotNull(boardTp, "boardTp must be provided");
        checkNotNull(name, "name must be provided");
        //checkNotNull(phone, "phone must be provided");
        checkNotNull(email, "email must be provided");
        checkNotNull(content, "content must be provided");

        Board board = new Board(boardTp, name, phone, email, content);
        boardRepository.save(board);
        return new BoardResult("등록되었습니다. ");
    }

    @Transactional(readOnly = true)
    public Optional<Board> findById(Long boardId) {
        checkNotNull(boardId, "boardId must be provided");

        return boardRepository.findById(boardId);
    }

}