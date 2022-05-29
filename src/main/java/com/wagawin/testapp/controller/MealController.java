package com.wagawin.testapp.controller;

import com.wagawin.testapp.dto.MealDto;
import com.wagawin.testapp.entity.Meal;
import com.wagawin.testapp.repository.MealRepository;
import com.wagawin.testapp.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MealController {
    private MealRepository mealRepository;
    private MealService mealService;

    public MealController(MealRepository mealRepository, MealService mealService) {
        this.mealRepository = mealRepository;
        this.mealService = mealService;
    }

    @PostMapping("/meal")
    public ResponseEntity<MealDto> save(@RequestBody MealDto mealDto) {
        Meal entity = new Meal(mealDto);
        return ResponseEntity.ok(new MealDto(this.mealRepository.save(entity)));
    }

    @GetMapping("/meal")
    public ResponseEntity<MealDto> get(@RequestParam("id") Integer id) {
        Optional<Meal> byId = mealRepository.findById(id);
        return byId.isPresent()
                ? ResponseEntity.ok(new MealDto(byId.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/cache/meal")
    public ResponseEntity<MealDto> cachedSave(@RequestBody MealDto mealDto) {
        return ResponseEntity.ok(this.mealService.save(mealDto));
    }

    @GetMapping("/cache/meal")
    public ResponseEntity<MealDto> cachedGet(@RequestParam("id") Integer id) {
        MealDto mealDto = mealService.get(id);
        return mealDto != null
                ? ResponseEntity.ok(mealDto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
