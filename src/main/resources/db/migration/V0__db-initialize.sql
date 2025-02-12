CREATE TABLE student
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255),
    age           INTEGER NOT NULL,
    email         VARCHAR(255),
    date_of_birth date,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

