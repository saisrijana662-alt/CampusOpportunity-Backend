package com.campusopportunity.repository;

import com.campusopportunity.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
}
