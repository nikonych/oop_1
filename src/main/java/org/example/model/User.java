package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.enums.Role;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NamedQuery(name = "User.authenticate", query = "from User where name = :name and password = :password")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String name;

    String password;

    public User(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    Boolean hasVoted = false;

    @Enumerated(EnumType.STRING)
    Role role;

    Integer voteCount = 0;

    public User() {

    }
}
