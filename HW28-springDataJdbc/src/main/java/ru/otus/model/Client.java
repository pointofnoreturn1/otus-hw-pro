package ru.otus.model;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("client")
public class Client {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    public String getStringPhones() {
        return phones.stream().map(Phone::toString).collect(Collectors.joining(" "));
    }
}
