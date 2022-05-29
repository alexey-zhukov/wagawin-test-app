package com.wagawin.testapp.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parent_summary")
@Getter @Setter @Accessors(chain = true)
public class ParentSummary {
    @Id private Integer amountOfChildren;
    private Integer amountOfPersons;

    public ParentSummary() {}

    public ParentSummary(CountAgg countAgg) {
        this.amountOfChildren = countAgg.getAggKey();
        this.amountOfPersons = countAgg.getCnt();
    }
}
