CREATE TABLE contact(
    contact_id SERIAL PRIMARY KEY,
    firstname VARCHAR,
    lastname VARCHAR,
    email VARCHAR,
    phone_number VARCHAR,
    date_of_birth DATE,
    image_url VARCHAR
);

CREATE TABLE anime(
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE user(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    username VARCHAR,
    password VARCHAR,
    authorities VARCHAR
);

INSERT INTO contact (firstname, lastname, email, phone_number, date_of_birth, image_url) VALUES ('First Name', 'Last Name', 'Email', 'Phone Number', '1985-01-25', 'Image URL');
INSERT INTO anime (id, name) values (1, 'Anime One');
-- Inserting record in user table will not work with security because every restart the bycript encoder
-- will generate new key, so it is beter to use save user from user controller to create user after
-- starting the project so you can test it.
-- INSERT INTO user (id ,name, username, password, authorities) values (1, 'Ahmed Samy', 'admin', 'admin', 'ROLE_ADMIN, ROLE_USER');
-- INSERT INTO user (id, name, username, password, authorities) values (2, 'Samy Ahmed', 'user', 'user', 'ROLE_USER');
