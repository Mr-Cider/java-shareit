DELETE FROM bookings;
DELETE FROM comments;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (name, email) VALUES ('firstName', 'firstEmail@test.ru');
INSERT INTO users (name, email) VALUES ('secondName', 'secondEmail@test.ru');
INSERT INTO users (name, email) VALUES ('thirdName', 'thirdEmail@test.ru');

INSERT INTO requests (description, requestor_id, request_date) VALUES
                     ('requestDescription', 1, now());
INSERT INTO requests (description, requestor_id, request_date) VALUES
    ('requestDescription2', 2, now());

INSERT INTO items (name, description, is_available, owner_id, request_id)
VALUES ('itemName', 'itemDescription', true, 1, 1);

INSERT INTO comments (text, item_id, author_id, created) VALUES
                     ('commentForTest', 1, 1, now());

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP - INTERVAL '12' DAY, CURRENT_TIMESTAMP - INTERVAL '10' DAY,
     1, 2, 'APPROVED');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP - INTERVAL '9' DAY, CURRENT_TIMESTAMP - INTERVAL '7' DAY,
     1, 2, 'REJECTED');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP + INTERVAL '10' DAY, CURRENT_TIMESTAMP + INTERVAL '12' DAY,
     1, 2, 'WAITING');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP + INTERVAL '2' DAY,
     1, 2, 'APPROVED');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP - INTERVAL '8' DAY, CURRENT_TIMESTAMP + INTERVAL '7' DAY,
     1, 2, 'REJECTED');

INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
    (CURRENT_TIMESTAMP + INTERVAL '5' DAY, CURRENT_TIMESTAMP + INTERVAL '8' DAY,
     1, 2, 'APPROVED');
