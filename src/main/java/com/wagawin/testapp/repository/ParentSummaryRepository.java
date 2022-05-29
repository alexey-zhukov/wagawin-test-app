package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.ParentSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParentSummaryRepository extends JpaSpecificationExecutor<ParentSummary>, JpaRepository<ParentSummary, Integer> {}
