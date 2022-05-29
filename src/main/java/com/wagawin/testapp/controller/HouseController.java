package com.wagawin.testapp.controller;

import com.wagawin.testapp.dto.HouseDto;
import com.wagawin.testapp.entity.House;
import com.wagawin.testapp.repository.HouseRepository;
import com.wagawin.testapp.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class HouseController {
    private HouseRepository houseRepository;
    private HouseService houseService;

    public HouseController(HouseRepository houseRepository, HouseService houseService) {
        this.houseRepository = houseRepository;
        this.houseService = houseService;
    }

    @GetMapping("/cache/house")
    public ResponseEntity<HouseDto> getSaved(@RequestParam("id") Integer id) {
        HouseDto house = this.houseService.get(id);
        return house != null
                ? ResponseEntity.ok(house)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/house")
    public ResponseEntity<HouseDto> get(@RequestParam("id") Integer id) {
        Optional<House> byId = this.houseRepository.findById(id);
        return byId.isPresent()
                ? ResponseEntity.ok(new HouseDto(byId.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/house")
    public ResponseEntity<HouseDto> save(@RequestBody HouseDto house) {
        return ResponseEntity.ok(this.houseService.save(house));
    }
}
