package com.wagawin.testapp.entity;


import com.wagawin.testapp.dto.ChildDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("D")
@Getter @Setter @Accessors(chain = true)
public class Daughter extends Child {
    private String hairColor;

    public Daughter() {}

    public Daughter(ChildDto child) {
        super(child);
    }
}
