package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.ChildDto;
import com.wagawin.testapp.dto.MealDto;
import com.wagawin.testapp.dto.PersonDto;
import com.wagawin.testapp.entity.Child;
import com.wagawin.testapp.entity.Daughter;
import com.wagawin.testapp.entity.Meal;
import com.wagawin.testapp.entity.Son;
import com.wagawin.testapp.repository.ChildRepository;
import com.wagawin.testapp.repository.MealRepository;
import com.wagawin.testapp.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.JoinType;
import java.util.Optional;

@CacheConfig(cacheNames = "children-cache")
@Service @Slf4j
public class ChildService {
    private ChildRepository childRepository;
    private PersonRepository personRepository;
    private MealRepository mealRepository;
    @Autowired private ChildService childService;

    public ChildService(ChildRepository childRepository,
                        PersonRepository personRepository, MealRepository mealRepository) {
        this.childRepository = childRepository;
        this.personRepository = personRepository;
        this.mealRepository = mealRepository;
    }

    @Cacheable(key = "#id")
    public ChildDto get(Integer id) {
        Optional<Child> byId = this.childRepository.findOne((root, query, criteriaBuilder) -> {
            root.join("parent");
            root.join("favouriteMeal");
            return criteriaBuilder.equal(root.get("id"), id);
        });
        return byId.map(ChildDto::new).orElse(null);
    }

    public ChildDto save(@RequestBody ChildDto child) {
        // if child id specified -- try to use existing child:
        Child entity = child.getId() != null
                ? childRepository.findById(child.getId()).orElseGet(() -> childByType(child))
                : childByType(child);
        Optional.of(child)
                .map(ChildDto::getParent)
                .map(PersonDto::getId)
                .ifPresent(id -> personRepository.findById(id)
                        .ifPresent(parent -> entity
                                .setParent(parent)
                                .setParentId(parent.getId())));
        return childService.put(new ChildDto(this.childRepository.save(entity)));
    }

    private Child childByType(@RequestBody ChildDto child) {
        switch (child.getType()) {
            case "Son":
                return new Son(child);
            case "Daughter":
                return new Daughter(child);
            default:
                return new Child(child);
        }
    }

    @CachePut(key = "#childId")
    public ChildDto addChildMeal(Integer childId, MealDto mealDto) {
        Optional<Child> child = childRepository.findOne((root, query, criteriaBuilder) -> {
            root.join("meals", JoinType.LEFT);
            root.join("parent", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("id"), childId);
        });
        if (!child.isPresent()) {
            log.error("Child with specified id not found");
            return null;
        }
        Optional<Meal> meal = mealRepository.findById(mealDto.getId());
        if (!meal.isPresent()) {
            log.error("Meal with specified id not found");
            return null;
        }

        return childService.put(new ChildDto(
                childRepository.save(child.get().addMeal(meal.get(), mealDto.getFavIndex()))));
    }

    @CachePut(key = "#child.id")
    public ChildDto put(ChildDto child) {
        return child;
    }
}