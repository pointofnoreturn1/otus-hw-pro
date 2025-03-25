package ru.otus.crm.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"address", "phones"})
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {
    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "client")
    private Address address;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "client", fetch = FetchType.EAGER)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;

        if (this.address != null) this.address.setClient(this);
        if (this.phones != null) this.phones.forEach(phone -> phone.setClient(this));
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Address newAddress = null;
        if (this.address != null) {
            newAddress = this.address.clone();
        }

        List<Phone> newPhones = null;
        if (this.phones != null) {
            newPhones = this.phones.stream().map(Phone::clone).toList();
        }

        return new Client(this.id, this.name, newAddress, newPhones);
    }
}
