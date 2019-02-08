package com.decision.engines.exporter.repository;

import com.decision.engines.exporter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
