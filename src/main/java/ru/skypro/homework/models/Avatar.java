package ru.skypro.homework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "avatars")
@DynamicInsert
@Getter
@ToString
@Setter
@NoArgsConstructor(force = true)
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image")
    private byte[] image;

    public Avatar(User user, byte[] image) {
        this.user = user;
        this.image = image;
    }
}