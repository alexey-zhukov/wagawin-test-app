package com.wagawin.testapp.entity;

import com.wagawin.testapp.dto.ChildDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "children")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("C")
@Getter @Setter @Accessors(chain = true)
public class Child {
    @Id
    @GeneratedValue(generator = "childs-sequence-generator")
    @GenericGenerator(
            name = "childs-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "children_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "100")
            }
    )
    private Integer id;
    private String name;
    private Integer age;
    @Column(name = "fav_meal_id")
    private Integer favouriteMealId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fav_meal_id", foreignKey = @ForeignKey(name = "child_fav_meal_fk"), insertable = false, updatable = false)
    private Meal favouriteMeal;

    @Column(name = "parent_id")
    private Integer parentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "child_parent_fk"), insertable = false, updatable = false)
    private Person parent;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildMeal> meals;

    public Child() {}

    public Child(ChildDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.age = dto.getAge();
    }

    public Child addMeal(Meal meal, Integer favIndex) {
        (this.meals != null ? this.meals : (this.meals = new ArrayList<>()))
                .add(new ChildMeal(this, meal, favIndex));
        // specify favourite meal:
        updateFavMealId();
        return this;
    }

    public Child removeMeal(Meal meal) {
        if (this.meals != null) {
            Iterator<ChildMeal> iterator = this.meals.iterator();
            while (iterator.hasNext()) {
                ChildMeal next = iterator.next();
                if (next.getMeal() != null && next.getMeal().getId().equals(meal.getId())) {
                    iterator.remove();
                    break;
                }
            }
            updateFavMealId();
        }
        return this;
    }

    private void updateFavMealId() {
        Integer minFavIndex = Integer.MAX_VALUE;
        Meal favMeal = null;
        for (ChildMeal childMeal : meals) {
            if (childMeal.getFavIndex() < minFavIndex) {
                favMeal = childMeal.getMeal();
                minFavIndex = childMeal.getFavIndex();
            }
        }
        this.setFavouriteMealId(favMeal != null ? favMeal.getId() : null);
        this.setFavouriteMeal(favMeal);
    }
}
