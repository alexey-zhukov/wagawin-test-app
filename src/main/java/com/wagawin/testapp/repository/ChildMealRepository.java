package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.ChildMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChildMealRepository extends JpaSpecificationExecutor<ChildMeal>, JpaRepository<ChildMeal, Integer> {

}
