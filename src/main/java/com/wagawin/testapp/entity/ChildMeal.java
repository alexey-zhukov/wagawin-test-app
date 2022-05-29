package com.wagawin.testapp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

// https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
@Entity
@Table(name = "child_meal")
@Getter @Setter @Accessors(chain = true)
public class ChildMeal {
    @EmbeddedId
    private ChildMealId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("childId")
    private Child child;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mealId")
    private Meal meal;

//    @MapsId("childId")
//    private Integer childId;
//    @MapsId("mealId")
//    private Integer mealId;

    @Column(name = "fav_idx")
    private Integer favIndex;

    public ChildMeal() {}

    public ChildMeal(Child child, Meal meal, Integer favIndex) {
        this.id = new ChildMealId(child.getId(), meal.getId());
        this.child = child;
        this.meal = meal;
        this.favIndex = favIndex;
    }
}
