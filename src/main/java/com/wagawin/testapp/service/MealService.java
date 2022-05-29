package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.MealDto;
import com.wagawin.testapp.entity.Meal;
import com.wagawin.testapp.repository.MealRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CacheConfig(cacheNames = "meals-cache")
@Service @Slf4j
public class MealService {
    private MealRepository mealRepository;
    @Autowired private MealService mealService;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Cacheable(key = "#id")
    public MealDto get(Integer id) {
        return this.mealRepository.findById(id).map(MealDto::new).orElse(null);
    }

    public MealDto save(@RequestBody MealDto meal) {
        return this.mealService.put(new MealDto(this.mealRepository.save(new Meal(meal))));
    }

    @CachePut(key = "#meal.id")
    public MealDto put(@RequestBody MealDto meal) {
        return meal;
    }
}
