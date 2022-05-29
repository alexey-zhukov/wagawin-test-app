package com.wagawin.testapp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @Accessors(chain = true)
public class ChildMealId implements Serializable {
    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "meal_id")
    private Integer mealId;

    public ChildMealId() {}

    public ChildMealId(Integer childId, Integer mealId) {
        this.childId = childId;
        this.mealId = mealId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildMealId that = (ChildMealId) o;
        return Objects.equals(childId, that.childId) &&
                Objects.equals(mealId, that.mealId);
    }

    @Override public int hashCode() {
        return Objects.hash(childId, mealId);
    }
}
