package com.decision.engines.exporter.repository;

import com.decision.engines.exporter.model.Bulk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BulkRepository extends JpaRepository<Bulk, Long> {
    Bulk findById(final UUID id);
}
