package com.github.tkpark.wind;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface WindLocationRepository extends Repository<WindLocation, Long> {

    Optional<WindLocation> findById(String id);

    List<WindLocation> findAll();

    @Query(value = "SELECT NX, NY" +
            " FROM wind_location" +
            " GROUP BY NX, NY ", nativeQuery = true)
    @Cacheable({"wind_location"})
    List<WindLocationInterface> findAllGroupBy();

    @Query(value = "SELECT NX, NY " +
            "  FROM wind_all_location " +
            " WHERE loc_group = :locGroup " +
            "   AND display = 'Y'" +
            " GROUP BY NX, NY ", nativeQuery = true)
    @Cacheable({"wind_all_location"})
    List<WindLocationInterface> findAllLocGroupBy(String locGroup);

    @Query(value = "SELECT NX, NY " +
            "  FROM wind_location " +
            " WHERE loc_group = :locGroup " +
            "   AND display = 'Y'" +
            " GROUP BY NX, NY ", nativeQuery = true)
    @Cacheable({"wind_location"})
    List<WindLocationInterface> findLocGroupBy(String locGroup);

}
