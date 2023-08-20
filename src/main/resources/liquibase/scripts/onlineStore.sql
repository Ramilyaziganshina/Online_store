-- liquibase formatted sql
-- changeset dsazonov:1

CREATE TABLE IF NOT EXISTS users
(
    user_id         bigserial PRIMARY KEY,
    user_role       VARCHAR(255) CHECK (user_role IN ('USER', 'ADMIN')),
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    phone      varchar(16),
    login   text UNIQUE NOT NULL,
    password   text        NOT NULL
    );

CREATE TABLE IF NOT EXISTS avatars
(
    image_id    bigserial PRIMARY KEY,
    user_id     bigint REFERENCES users(user_id) ON DELETE CASCADE,
    image       bytea
    );

CREATE TABLE IF NOT EXISTS ads
(
    id          bigserial PRIMARY KEY,
    user_id     bigint REFERENCES users (user_id),
    title       text           NOT NULL,
    description text           NOT NULL,
    price       decimal(17, 2) NOT NULL CHECK (price > 0::decimal)
    );

CREATE TABLE IF NOT EXISTS images
(
    image_id  bigserial  PRIMARY KEY,
    ad_id     bigint  REFERENCES ads(id),
    image  bytea
    );

CREATE TABLE IF NOT EXISTS comments
(
    comment_id bigint PRIMARY KEY,
    create_time timestamp,
    text_comment text,
    id_ad         bigint,
    user_id bigint,

    CONSTRAINT comments_id_ad_fkey
    FOREIGN KEY (id_ad)
    REFERENCES ads (id)
    ON DELETE CASCADE,

    CONSTRAINT user_id_pk FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON Delete CASCADE
    );

create sequence if not exists hibernate_sequence;

alter sequence hibernate_sequence owner to "Graduation";
