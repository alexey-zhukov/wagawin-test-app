package com.wagawin.testapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wagawin.testapp.entity.Person;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data public class PersonDto implements Serializable {
    private Integer id;
    private String name;
    private Integer age;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HouseDto house;

    public PersonDto() {}

    public PersonDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.age = person.getAge();
    }
}
