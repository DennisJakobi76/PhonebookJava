CREATE TABLE phonebookentries (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_prefix VARCHAR(20),
    phone_number VARCHAR(20)
);

INSERT INTO phonebookentries (id, first_name, last_name, phone_prefix, phone_number)
VALUES
    (1, 'Anna', 'Schneider', '+49', '1512345678'),
    (2, 'Markus', 'Meier', '+49', '1729876543'),
    (3, 'Laura', 'Fischer', '+43', '6601234567'),
    (4, 'Tom', 'Becker', '+49', '1604455667'),
    (5, 'Sophie', 'Weber', '+41', '781234567'),
    (6, 'Lukas', 'Huber', '+43', '6998765432'),
    (7, 'Mia', 'Wolf', '+49', '1579876543'),
    (10, 'Felix', 'Schmid', '+43', '681234567'),
    (11, 'Nina', 'Hartmann', '+49', '1523456789'),
    (12, 'Tim', 'Lang', '+49', '1518765432'),
    (13, 'Clara', 'Frank', '+43', '664123456'),
    (14, 'Julian', 'Graf', '+49', '1709876543'),
    (15, 'Emma', 'König', '+41', '765432198'),
    (16, 'Moritz', 'Lehmann', '+49', '1598765432'),
    (19, 'Lea', 'Brandt', '+49', '1742345678'),
    (20, 'Ben', 'Lorenz', '+41', '779876543');