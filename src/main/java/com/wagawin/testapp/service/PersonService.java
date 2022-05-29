package com.wagawin.testapp.service;

import com.wagawin.testapp.dto.HouseDto;
import com.wagawin.testapp.dto.PersonDto;
import com.wagawin.testapp.entity.Person;
import com.wagawin.testapp.repository.HouseRepository;
import com.wagawin.testapp.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@CacheConfig(cacheNames = "persons-cache")
@Service @Slf4j
public class PersonService {
    private PersonRepository personRepository;
    private HouseRepository houseRepository;
    @Autowired private PersonService personService;

    public PersonService(PersonRepository personRepository,
                         HouseRepository houseRepository) {
        this.personRepository = personRepository;
        this.houseRepository = houseRepository;
    }

    @Cacheable(key = "#id")
    public PersonDto get(Integer id) {
        return this.personRepository.findById(id).map(PersonDto::new).orElse(null);
    }

    public PersonDto save(PersonDto person) {
        Person entity = new Person(person);
        Optional.of(person)
                .map(PersonDto::getHouse)
                .map(HouseDto::getId)
                .ifPresent(id -> houseRepository.findById(id)
                        .ifPresent(entity::setHouse));
        return personService.put(new PersonDto(this.personRepository.save(entity))
                .setHouse(HouseDto.of(entity.getHouse())));
    }

    @CachePut(key = "#person.id")
    public PersonDto put(PersonDto person) {
        return person;
    }
}
