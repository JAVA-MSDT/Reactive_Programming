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

INSERT INTO contact (firstname, lastname, email, phone_number, date_of_birth, image_url) VALUES ('First Name', 'Last Name', 'Email', 'Phone Number', '1985-01-25', 'Image URL');
INSERT INTO anime (id, name) values (1, 'Anime One');
