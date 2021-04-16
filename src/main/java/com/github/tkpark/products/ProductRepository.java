package com.github.tkpark.products;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface ProductRepository extends Repository<Product, Long> {

    Optional<Product> findById(long id);

    List<Product> findAllByOrderBySeqDesc();

}