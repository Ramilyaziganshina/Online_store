package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByAd_Id(Long id);

    Optional<Comment> findCommentByAdAndId(Ad ad, Long id);

    Optional<Comment> findCommentById(Long id);

    Optional<Comment> findByIdAndAdId(Long commentID, Long adPk);
}