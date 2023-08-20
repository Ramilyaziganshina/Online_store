package ru.skypro.homework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User author;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "ad", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Image image;

    public Ad(User author, BigDecimal price, String title, String description, Image image) {
        this.author = author;
        this.price = price;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}