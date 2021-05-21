package com.github.tkpark.image;

import com.github.tkpark.wind.RoadMaster;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface ImageRepository extends Repository<RoadMaster, Long> {

    Optional<RoadMaster> findByRoad(String road);

}
