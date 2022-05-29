package com.wagawin.testapp.entity;

import com.wagawin.testapp.dto.PersonDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "persons")
@Getter @Setter @Accessors(chain = true)
public class Person {
    @Id
    @GeneratedValue(generator = "persons-sequence-generator")
    @GenericGenerator(
            name = "persons-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "persons_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "100")
            }
    )
    private Integer id;
    private String name;
    private Integer age;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", foreignKey = @ForeignKey(name = "person_house_fk"), unique = true)
    private House house;

    public Person() {}

    public Person(PersonDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.age = dto.getAge();
    }

}
