package com.github.tkpark.wind;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface WindLocationDataRepository extends Repository<WindLocationData, Long> {

    Optional<WindLocationData> findById(String id);

    List<WindLocationData> findAll();

}
