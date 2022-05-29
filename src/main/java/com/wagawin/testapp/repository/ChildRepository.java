package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.Child;
import com.wagawin.testapp.entity.CountAgg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildRepository extends JpaSpecificationExecutor<Child>, JpaRepository<Child, Integer> {
    @Query(value = "select t.cnt as aggKey, count(parent_id) as cnt from (\n" +
            "   select parent_id, count(id) as cnt from children group by parent_id\n" +
            ") as t group by t.cnt", nativeQuery = true)
    public List<CountAgg> calcParentSummary();

}
