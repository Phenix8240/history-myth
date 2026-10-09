package com.myth.stories.repository;

import com.myth.stories.entity.DeityStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeityStoryRepository extends JpaRepository<DeityStory, Long> {

    List<DeityStory> findByDeityIdOrderByDisplayOrderAsc(Long deityId);
}