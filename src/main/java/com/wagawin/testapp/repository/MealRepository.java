package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MealRepository extends JpaSpecificationExecutor<Meal>, JpaRepository<Meal, Integer> {
    @Query("select min(id) from Meal")
    Integer getMinId();

    @Query("select max(id) from Meal")
    Integer getMaxId();

}
