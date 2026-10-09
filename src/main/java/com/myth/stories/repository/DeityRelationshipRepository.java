package com.myth.stories.repository;

import com.myth.stories.entity.DeityRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeityRelationshipRepository extends JpaRepository<DeityRelationship, Long> {

    // this deity -> others
    List<DeityRelationship> findBySourceDeityId(Long id);

    // others -> this deity
    List<DeityRelationship> findByTargetDeityId(Long id);
}