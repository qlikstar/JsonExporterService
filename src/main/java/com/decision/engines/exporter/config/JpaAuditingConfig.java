package com.decision.engines.exporter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * This class in needed to configure Auditing for the JPA objects
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
