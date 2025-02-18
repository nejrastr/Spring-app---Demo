ALTER TABLE profesor
    ADD CONSTRAINT uc_profesor_email UNIQUE (email);