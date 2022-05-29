package com.wagawin.testapp.controller;

import com.wagawin.testapp.dto.HouseDto;
import com.wagawin.testapp.dto.ParentSummaryDto;
import com.wagawin.testapp.dto.PersonDto;
import com.wagawin.testapp.entity.Person;
import com.wagawin.testapp.repository.PersonRepository;
import com.wagawin.testapp.service.ParentSummaryService;
import com.wagawin.testapp.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import java.util.Optional;

@RestController
public class PersonController {
    private PersonRepository personRepository;
    private PersonService personService;
    private ParentSummaryService parentSummaryService;

    public PersonController(PersonRepository personRepository,
                            PersonService personService,
                            ParentSummaryService parentSummaryService) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.parentSummaryService = parentSummaryService;
    }

    @PostMapping("/person")
    public ResponseEntity<PersonDto> person(@RequestBody PersonDto person) {
        return ResponseEntity.ok(personService.save(person));
    }

    @GetMapping("/person")
    public ResponseEntity<PersonDto> person(@RequestParam("id") Integer id) {
        Optional<Person> byId = this.personRepository.findOne((root, query, criteriaBuilder) -> {
            root.join("house", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("id"), id);
        });
        return byId.isPresent()
                ? ResponseEntity.ok(new PersonDto(byId.get()).setHouse(HouseDto.of(byId.get().getHouse())))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/cache/person")
    public ResponseEntity<PersonDto> cachedPerson(@RequestParam("id") Integer id) {
        PersonDto personDto = this.personService.get(id);
        return personDto != null
                ? ResponseEntity.ok(personDto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/persons/children")
    public ResponseEntity<ParentSummaryDto> personsChildren() {
        return ResponseEntity.ok(this.parentSummaryService.summary());
    }
}
