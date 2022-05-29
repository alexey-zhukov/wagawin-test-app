package com.wagawin.testapp.entity;

import com.wagawin.testapp.dto.MealDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "meals")
@Getter @Setter @Accessors(chain = true)
public class Meal {
    @Id
    @GeneratedValue(generator = "meals-sequence-generator")
    @GenericGenerator(
            name = "meals-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "meals_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "100")
            }
    )
    private Integer id;
    private String name;
    private Date invented;

    public Meal() {}

    public Meal(MealDto mealDto) {
        this.id = mealDto.getId();
        this.name = mealDto.getName();
        this.invented = mealDto.getInvented();
    }

    @Override public String toString() {
        return "Meal: " + this.id;
    }
}
