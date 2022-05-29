package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaSpecificationExecutor<Person>, JpaRepository<Person, Integer> {
    @Query("select min(id) from Person")
    Integer getMinId();

    @Query("select max(id) from Person")
    Integer getMaxId();
}
