package com.nameslowly.coinauctions.auth.domain.repository;

import com.nameslowly.coinauctions.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
