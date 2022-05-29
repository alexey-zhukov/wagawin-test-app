package com.wagawin.testapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wagawin.testapp.entity.Child;
import com.wagawin.testapp.entity.Daughter;
import com.wagawin.testapp.entity.Son;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data @Accessors(chain = true)
public class ColorDto implements Serializable {
    @JsonIgnore
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hairColor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bicycleColor;

    public ColorDto(Child child) {
        this.id = child.getId();
        if (child instanceof Son)
            this.bicycleColor = ((Son) child).getBicycleColor();
        else if (child instanceof Daughter)
            this.hairColor = ((Daughter) child).getHairColor();
    }
}
