package com.github.tkpark.wind;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface WindRepository extends Repository<Wind, Long> {

    Wind save(Wind item);

}
