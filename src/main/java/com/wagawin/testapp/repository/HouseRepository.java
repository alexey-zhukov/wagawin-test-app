package com.wagawin.testapp.repository;

import com.wagawin.testapp.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HouseRepository extends JpaSpecificationExecutor<House>, JpaRepository<House, Integer> {
}
