package com.github.tkpark.users;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public interface UserRepository extends Repository<User, Long> {

    User save(User item);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

}
