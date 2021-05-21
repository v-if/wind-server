package com.github.tkpark.wind;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface WindRepository extends Repository<Wind, Long> {

    List<Wind> findAllByBaseDateAndBaseTime(String baseDate, String baseTime);

    Wind findByBaseDateAndBaseTimeAndNxAndNy(String baseDate, String baseTime, String nx, String ny);

    List<Wind> findAll();

    Wind save(Wind item);

}
