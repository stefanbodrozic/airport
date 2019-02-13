DROP SCHEMA IF EXISTS airport;
CREATE SCHEMA airport DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE airport;

CREATE TABLE airports (
	id INT AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO airports (name) VALUES ('Nikola Tesla (Belgrade)');
INSERT INTO airports (name) VALUES ('Domodedovo (Moscow)');
INSERT INTO airports (name) VALUES ('JFK (New York)');
INSERT INTO airports (name) VALUES ('Narita (Tokio)');
INSERT INTO airports (name) VALUES ('Fiumicino (Rome)');
INSERT INTO airports (name) VALUES ('Charles de Gaulle (France)');

CREATE TABLE flights(
	id INT AUTO_INCREMENT,
    number_of_flight VARCHAR(10) NOT NULL,
    date_of_departure DATETIME DEFAULT CURRENT_TIMESTAMP,
    arrival_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_departure_airport INT NOT NULL,
    id_incoming_airport INT NOT NULL,
    total_seat_number INT NOT NULL,
    price FLOAT NOT NULL,
    deleted BOOLEAN DEFAULT false,
    FOREIGN KEY(id_departure_airport) REFERENCES airports(id),
    FOREIGN KEY(id_incoming_airport) REFERENCES airports(id),
    PRIMARY KEY(id)
);

INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F1', '2019-01-01 20:00:00', '2019-01-01 22:30:00', 1, 2, 10, 999);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F2', '2019-01-01 23:00:00', '2019-01-02 01:30:00', 2, 3, 20, 1200);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F3', '2019-01-02 05:00:00', '2019-01-02 10:30:00', 3, 4, 30, 2000);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F4', '2019-01-02 05:45:00', '2019-01-02 07:15:00', 2, 1, 20, 1000);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F5', '2019-01-03 20:00:00', '2019-01-03 22:30:00', 1, 2, 10, 1000);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F6', '2019-04-01 20:00:00', '2019-04-01 22:30:00', 1, 2, 10, 999);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F7', '2019-04-01 23:00:00', '2019-04-02 01:30:00', 2, 3, 20, 1200);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F8', '2019-04-02 05:00:00', '2019-04-02 10:30:00', 3, 4, 30, 2000);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F9', '2019-04-02 05:45:00', '2019-04-02 07:15:00', 2, 1, 20, 1500);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F10', '2019-04-03 20:00:00', '2019-04-03 22:30:00', 1, 2, 10, 800);
INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price)
	VALUES ('F11', '2019-04-12 03:00:00', '2019-04-13 04:30:00', 3, 2, 30, 500);


CREATE TABLE users (
	id INT AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    blocked BOOLEAN DEFAULT false,
    deleted BOOLEAN DEFAULT false,
    PRIMARY KEY(id)
);

INSERT INTO users (username, password, role) VALUES ('pera', 'pera', 'ADMIN');
INSERT INTO users (username, password) VALUES ('marko', 'marko');
INSERT INTO users (username, password) VALUES ('djura', 'djura');

CREATE TABLE reserved_ticket (
	id INT AUTO_INCREMENT,
    id_departure_flight INT NOT NULL,
    departure_flight_seat_number INT NOT NULL,
    id_incoming_flight INT,
    incoming_flight_seat_number INT,
    reservation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    firstname CHAR(20) NOT NULL,
    lastname CHAR(30) NOT NULL,
    bought_ticket BOOLEAN DEFAULT false,
    price FLOAT NOT NULL,
    deleted BOOLEAN DEFAULT false,
    PRIMARY KEY(id),
    FOREIGN KEY(id_departure_flight) REFERENCES flights(id),
    FOREIGN KEY(id_incoming_flight) REFERENCES flights(id),
    FOREIGN KEY(user_id) REFERENCES users(id)    
);

INSERT INTO reserved_ticket(id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, price, bought_ticket)
	VALUES(1, 1, null, null, 1, 'Petar', 'Petrovic', 999, true);
INSERT INTO reserved_ticket(id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, price, bought_ticket)
	VALUES(2, 3, null, null, 2, 'Marko', 'Markovic', 1200, true);
INSERT INTO reserved_ticket(id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, price, bought_ticket)
	VALUES(1, 2, 4, 5, 1, 'Petar', 'Petrovic', 1999, true);
INSERT INTO reserved_ticket(id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, price, bought_ticket)
	VALUES(3, 20, null, null, 3, 'Djura', 'Djuric', 2000, false);
INSERT INTO reserved_ticket(id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, price, bought_ticket)
	VALUES(5, 10, null, null, 1, 'Djura', 'Djuric', 1000, true);

CREATE TABLE confirm_ticket (
	id INT AUTO_INCREMENT,
    id_reserved_ticket INT NOT NULL,
    confirm_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT false,
    PRIMARY KEY(id),
    FOREIGN KEY(id_reserved_ticket) REFERENCES reserved_ticket(id)
);

INSERT INTO confirm_ticket(id_reserved_ticket) VALUES (1);
INSERT INTO confirm_ticket(id_reserved_ticket) VALUES (2);
INSERT INTO confirm_ticket(id_reserved_ticket) VALUES (3);
INSERT INTO confirm_ticket(id_reserved_ticket) VALUES (5);