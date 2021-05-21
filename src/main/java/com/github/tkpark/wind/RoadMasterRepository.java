package com.github.tkpark.wind;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface RoadMasterRepository extends Repository<RoadMaster, Long> {

    List<RoadMaster> findAllByOrderBySeqAsc();

}
