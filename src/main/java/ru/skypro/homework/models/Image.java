package ru.skypro.homework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "images")
@DynamicInsert
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Column(name = "image")
    private byte[] image;


    public Image(Ad ad, byte[] image) {
        this.ad = ad;
        this.image = image;
    }
}