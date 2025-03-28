package ru.otus.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("phone")
public class Phone {
    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;

    private Long clientId;

    @Override
    public String toString() {
        return number;
    }
}
