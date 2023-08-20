package ru.skypro.homework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicInsert;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    @Column(name = "login")
    private String username;


    private String password;

    @Column(name = "user_role")
    @Check(constraints = "role IN ('USER', 'ADMIN')")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    //@PrimaryKeyJoinColumn
    private Avatar avatar;

    public User(String firstName,
                String lastName,
                String phone,
                String username,
                String password,
                Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}