package com.wagawin.testapp.dto;

import com.wagawin.testapp.entity.House;
import com.wagawin.testapp.entity.HouseType;
import lombok.Data;

import java.io.Serializable;

@Data public class HouseDto implements Serializable {
    private Integer id;
    private String address;
    private String zipCode;
    private HouseType type;

    public HouseDto() {}

    public HouseDto(House house) {
        this.id = house.getId();
        this.address = house.getAddress();
        this.zipCode = house.getZipCode();
        this.type = house.getType();
    }

    public static HouseDto of(House house) {
        return house == null ? null : new HouseDto(house);
    }
}
