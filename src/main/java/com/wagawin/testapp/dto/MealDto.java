package com.wagawin.testapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wagawin.testapp.entity.Meal;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data public class MealDto implements Serializable {
    private Integer id;
    private String name;
    private Date invented;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer favIndex;

    public MealDto() {}

    public MealDto(Meal meal) {
        this.id = meal.getId();
        this.name = meal.getName();
        this.invented = meal.getInvented();
    }
}
