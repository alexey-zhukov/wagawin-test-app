package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.HouseDto;
import com.wagawin.testapp.entity.House;
import com.wagawin.testapp.repository.HouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "houses-cache")
@Service @Slf4j public class HouseService {
    private HouseRepository houseRepository;
    @Autowired private HouseService houseService;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Cacheable(key = "#id")
    public HouseDto get(Integer id) {
        return this.houseRepository.findById(id).map(HouseDto::new).orElse(null);
    }

    public HouseDto save(HouseDto house) {
        return houseService.put(new HouseDto(this.houseRepository.save(new House(house))));
    }

    @CachePut(key = "#house.id")
    public HouseDto put(HouseDto house) {
        return house;
    }
}
