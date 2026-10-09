package com.myth.stories.repository;

import com.myth.stories.entity.Deity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeityRepository extends JpaRepository<Deity, Long> {

    Optional<Deity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Deity> findAllByOrderByOrderNumberAscNameAsc();

    List<Deity> findByCategoryIgnoreCaseOrderByOrderNumberAscNameAsc(String category);
}