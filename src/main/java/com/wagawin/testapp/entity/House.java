package com.wagawin.testapp.entity;

import com.wagawin.testapp.dto.HouseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "houses")
@Getter @Setter @Accessors(chain = true)
public class House {
    @Id
    @GeneratedValue(generator = "houses-sequence-generator")
    @GenericGenerator(
            name = "houses-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "houses_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "100")
            }
    )
    private Integer id;
    private String address;
    private String zipCode;
    // can be replaced with ordinal to increase performance, but introduce readability issue:
    @Enumerated(EnumType.STRING)
    private HouseType type;

    public House() {}

    public House(HouseDto houseDto) {
        this.id = houseDto.getId();
        this.address = houseDto.getAddress();
        this.zipCode = houseDto.getZipCode();
        this.type = houseDto.getType();
    }
}
