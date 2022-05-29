package com.wagawin.testapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data @Accessors(chain = true)
public class ParentSummaryDto {
    private Integer[] parentSummary;

    public ParentSummaryDto() {}

    public ParentSummaryDto(Integer[] parentSummary) {
        this.parentSummary = parentSummary;
    }
}
