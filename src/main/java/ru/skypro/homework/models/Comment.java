package ru.skypro.homework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@ToString
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "create_time")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_ad")
    private Ad ad;

    @Column(name = "text_comment")
    private String text;

    public Comment(User author, LocalDateTime createdAt, String text) {
        this.author = author;
        this.createdAt = createdAt;
        this.text = text;
    }
}