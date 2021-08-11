package com.github.tkpark.wind;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public interface WindForecastRepository extends Repository<WindForecast, Long> {

    @Transactional
    @Modifying
    @Query(value =
            "DELETE FROM wind_forecast " +
            " WHERE base_date < :baseDate ", nativeQuery = true)
    @Cacheable({"deleteOldData"})
    int deleteOldData(@Param("baseDate") String baseDate);

}
