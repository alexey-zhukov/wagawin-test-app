package com.wagawin.testapp.dto;

import com.wagawin.testapp.entity.Child;
import lombok.Data;

import java.io.Serializable;

@Data public class ChildDto implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
    private String type;

    private PersonDto parent;
    private MealDto favouriteMeal;

    public ChildDto() {}

    public ChildDto(Child child) {
        this.id = child.getId();
        this.name = child.getName();
        this.age = child.getAge();
        this.type = child.getClass().getSimpleName();

        this.favouriteMeal = child.getFavouriteMeal() != null ? new MealDto(child.getFavouriteMeal()) : null;
        this.parent = child.getParent() != null ? new PersonDto(child.getParent()) : null;
    }
}
