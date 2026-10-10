package com.campusopportunity.repository;

import com.campusopportunity.entity.SavedOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedOpportunityRepository extends JpaRepository<SavedOpportunity, Long> {

    List<SavedOpportunity> findByStudentId(Long studentId);

    boolean existsByStudentIdAndOpportunityId(Long studentId, Long opportunityId);

    void deleteByOpportunityId(Long opportunityId);
}
