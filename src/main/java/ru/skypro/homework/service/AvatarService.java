package ru.skypro.homework.service;

import ru.skypro.homework.models.Avatar;

import java.util.Optional;

public interface AvatarService {
    public Optional<Avatar> findAvatar(Long id);
}