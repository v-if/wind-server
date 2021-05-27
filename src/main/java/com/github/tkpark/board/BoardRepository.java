package com.github.tkpark.board;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface BoardRepository extends Repository<Board, Long> {

    Board save(Board item);

    Optional<Board> findById(long id);

}