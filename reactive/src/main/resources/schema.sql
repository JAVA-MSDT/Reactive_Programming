CREATE TABLE contact(
    contact_id SERIAL PRIMARY KEY,
    firstname VARCHAR,
    lastname VARCHAR,
    email VARCHAR,
    phone_number VARCHAR,
    date_of_birth DATE,
    image_url VARCHAR
);

INSERT INTO contact (firstname, lastname, email, phone_number, date_of_birth, image_url) VALUES ('First Name', 'Last Name', 'Email', 'Phone Number', '1985-01-25', 'Image URL');
