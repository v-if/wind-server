package com.github.tkpark.wind;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface WindLocationDataRepository extends Repository<WindLocationData, Long> {

    Optional<WindLocationData> findById(String id);

    List<WindLocationData> findAll();

    @Query(value = " SELECT WL.id " +
            "     , WL.location1 " +
            "     , WL.location2 " +
            "     , WL.location3 " +
            "     , WD.base_date " +
            "     , WD.base_time " +
            "     , WL.nx " +
            "     , WL.ny " +
            "     , WL.latitude " +
            "     , WL.longitude " +
            "     , WL.distance " +
            "     , WD.pty " +
            "     , WD.reh " +
            "     , WD.rn1 " +
            "     , WD.t1h " +
            "     , WD.uuu " +
            "     , WD.vvv " +
            "     , WD.vec " +
            "     , WD.wsd " +
            "     , WD.wd16 " +
            "     , DATE_FORMAT(WD.create_date, '%Y-%m-%d %H:%i:%s') AS 'create_date' " +
            "  FROM ( " +
            "         SELECT substr(wd.datetime, 1, 8) AS 'base_date' " +
            "              , substr(wd.datetime, 9, 12) AS 'base_time' " +
            "              , wl.id, wl.location1, wl.location2, wl.location3, wl.nx, wl.ny, wl.latitude, wl.longitude, wl.distance " +
            "          FROM ( " +
            "                 SELECT a.id, a.location1, a.location2, a.location3, a.nx, a.ny, a.latitude, a.longitude, a.distance " +
            "                   FROM ( " +
            "                          SELECT id, location1, location2, location3, nx, ny, latitude, longitude " +
            "                               , TRUNCATE(111.111 * " +
            "                                 DEGREES(ACOS(LEAST(COS(RADIANS(latitude)) " +
            "                                 * COS(RADIANS(:latitude)) " +
            "                                 * COS(RADIANS(longitude - :longitude)) " +
            "                                 + SIN(RADIANS(latitude)) " +
            "                                 * SIN(RADIANS(:latitude)), 1.0))) * 1000, 0) AS distance " +
            "                            FROM wind_location " +
            "                           WHERE display = 'Y' " +
            "                          HAVING TRUNCATE(111.111 * " +
            "                                 DEGREES(ACOS(LEAST(COS(RADIANS(latitude)) " +
            "                                 * COS(RADIANS(:latitude)) " +
            "                                 * COS(RADIANS(longitude - :longitude)) " +
            "                                 + SIN(RADIANS(latitude)) " +
            "                                 * SIN(RADIANS(:latitude)), 1.0))) * 1000, 0) <= 3000 " +
            "                        ) a " +
            "               ) wl " +
            "             , ( " +
            "                 SELECT MAX(concat(base_date, base_time)) AS 'datetime', nx, ny " +
            "                   FROM wind_data " +
            "                  GROUP BY nx, ny " +
            "               ) wd " +
            "         WHERE wl.nx = wd.nx " +
            "           AND wl.ny = wd.ny " +
            "       ) WL INNER JOIN wind_data WD ON WL.base_date = WD.base_date AND WL.base_time = WD.base_time AND WL.nx = WD.nx AND WL.ny = WD.ny " +
            " ORDER BY WL.distance " +
            " LIMIT 10 ", nativeQuery = true)
    @Cacheable({"wind_location_data_distance"})
    List<WindLocationData> findAllWindLocationDataDistance(String latitude, String longitude);

}
