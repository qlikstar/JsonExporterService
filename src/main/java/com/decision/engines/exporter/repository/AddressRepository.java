package com.decision.engines.exporter.repository;

import com.decision.engines.exporter.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
