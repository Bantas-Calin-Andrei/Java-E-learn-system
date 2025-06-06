psql (14.18 (Homebrew), server 17.2)

CREATE TABLE utilizatori (
email VARCHAR(255) PRIMARY KEY,
nume VARCHAR(255) NOT NULL,
tip VARCHAR(50) NOT NULL
);

CREATE TABLE profesori (
email VARCHAR(255) PRIMARY KEY,
specializare VARCHAR(255) NOT NULL,
FOREIGN KEY (email) REFERENCES utilizatori(email)
);

CREATE TABLE cursanti (
email VARCHAR(255) PRIMARY KEY,
nivel VARCHAR(50) NOT NULL,
FOREIGN KEY (email) REFERENCES utilizatori(email)
);

CREATE TABLE curs (
id SERIAL PRIMARY KEY,
titlu VARCHAR(255) NOT NULL,
descriere TEXT,
profesor_email VARCHAR(255),
FOREIGN KEY (profesor_email) REFERENCES utilizatori(email)
);

CREATE TABLE quiz (
id SERIAL PRIMARY KEY,
titlu VARCHAR(255) NOT NULL,
descriere TEXT
);

CREATE TABLE intrebare (
id SERIAL PRIMARY KEY,
text TEXT NOT NULL,
quiz_id INT,
punctaj INT,
FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE curs_cursanti (
curs_id INT,
cursant_email VARCHAR(255),
PRIMARY KEY (curs_id, cursant_email),
FOREIGN KEY (curs_id) REFERENCES curs(id),
FOREIGN KEY (cursant_email) REFERENCES cursanti(email)
);

CREATE TABLE audit (
id SERIAL PRIMARY KEY,
action VARCHAR(255) NOT NULL,
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);