package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> getAllByAuthor(User user);

    List<Ad> getAllByAuthorId(Long id);
}