package com.wagawin.testapp.entity;


import com.wagawin.testapp.dto.ChildDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
@Getter @Setter @Accessors(chain = true)
public class Son extends Child {
    private String bicycleColor;

    public Son() {}

    public Son(ChildDto dto) {
        super(dto);
    }
}
