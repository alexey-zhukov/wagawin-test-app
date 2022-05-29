package com.wagawin.testapp.controller;

import com.wagawin.testapp.dto.ChildDto;
import com.wagawin.testapp.dto.ColorDto;
import com.wagawin.testapp.dto.MealDto;
import com.wagawin.testapp.entity.Child;
import com.wagawin.testapp.repository.ChildMealRepository;
import com.wagawin.testapp.repository.ChildRepository;
import com.wagawin.testapp.repository.MealRepository;
import com.wagawin.testapp.repository.PersonRepository;
import com.wagawin.testapp.service.ChildService;
import com.wagawin.testapp.service.ColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import java.util.Optional;

@RestController
public class ChildController {
    private ChildService childService;
    private ColorService colorService;
    private ChildRepository childRepository;
    private ChildMealRepository childMealRepository;
    private MealRepository mealRepository;
    private PersonRepository personRepository;

    public ChildController(ChildService childService, ColorService colorService,
                           ChildRepository childRepository,
                           ChildMealRepository childMealRepository,
                           MealRepository mealRepository,
                           PersonRepository personRepository) {
        this.childService = childService;
        this.colorService = colorService;
        this.childRepository = childRepository;
        this.childMealRepository = childMealRepository;
        this.mealRepository = mealRepository;
        this.personRepository = personRepository;
    }

    @PostMapping("/child")
    public ResponseEntity<ChildDto> cachedSave(@RequestBody ChildDto child) {
        return ResponseEntity.ok(this.childService.save(child));
    }

    @PostMapping("/child/meal")
    @Transactional
    public ResponseEntity<String> cachedChildMeal(@RequestParam("childId") Integer childId, @RequestBody MealDto mealDto) {
        ChildDto childDto = this.childService.addChildMeal(childId, mealDto);
        return childDto != null
                ? ResponseEntity.ok("Saved")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with specified id not found");
    }

    @PostMapping("/color") // better use @PathVariable("id")
    public ResponseEntity<ColorDto> color(@RequestParam("childId") Integer id, @RequestParam("color") String color) {
        ColorDto dto = this.colorService.save(id, color);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/child/info") // better use @PathVariable("id")
    public ResponseEntity<ChildDto> child(@RequestParam("id") Integer id) {
        Optional<Child> byId = this.childRepository.findOne((root, query, criteriaBuilder) -> {
            root.join("parent", JoinType.LEFT);
            root.join("favouriteMeal", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("id"), id);
        });
        return byId.isPresent()
                ? ResponseEntity.ok(new ChildDto(byId.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/cache/child/info") // better use @PathVariable("id")
    public ResponseEntity<ChildDto> cachedGet(@RequestParam("id") Integer id) {
        ChildDto childDto = this.childService.get(id);
        return childDto != null
                ? ResponseEntity.ok(childDto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/color")
    public ResponseEntity<ColorDto> color(@RequestParam("id") Integer id) {
        Optional<Child> byId = this.childRepository.findById(id);
        return byId.isPresent()
                ? ResponseEntity.ok(new ColorDto(byId.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/cache/color")
    public ResponseEntity<ColorDto> cachedColor(@RequestParam("id") Integer id) {
        ColorDto dto = this.colorService.get(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
