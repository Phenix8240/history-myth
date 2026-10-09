package com.myth.stories.repository;

import com.myth.stories.entity.DeityDetail;
import com.myth.stories.entity.DetailType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeityDetailRepository extends JpaRepository<DeityDetail, Long> {

    List<DeityDetail> findByDeityIdOrderByDisplayOrderAsc(Long deityId);

    List<DeityDetail> findByDeityIdAndTypeOrderByDisplayOrderAsc(Long deityId, DetailType type);
}