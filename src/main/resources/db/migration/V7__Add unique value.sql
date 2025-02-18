ALTER TABLE course
    ADD CONSTRAINT uc_course_name UNIQUE (name);