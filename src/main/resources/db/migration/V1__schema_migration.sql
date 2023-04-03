drop table if exists BOOKS, COMMENTS CASCADE;
drop table if exists GENRES;
drop table if exists AUTHORS;

CREATE TABLE AUTHORS
(
    ID             BIGSERIAL        PRIMARY KEY,
    NAME           VARCHAR(255)     NOT NULL
);


CREATE TABLE GENRES
(
    ID             BIGSERIAL        PRIMARY KEY,
    NAME           VARCHAR(255)     NOT NULL
);

CREATE TABLE BOOKS
(
    ID             BIGSERIAL        PRIMARY KEY,
    NAME           VARCHAR(255)     NOT NULL,
    AUTHOR_ID      BIGINT           NOT NULL,
    GENRE_ID       BIGINT           NOT NULL,

    CONSTRAINT fk_authors
        FOREIGN KEY (AUTHOR_ID)
            REFERENCES AUTHORS (ID),

    CONSTRAINT fk_GENRES
        FOREIGN KEY (GENRE_ID)
            REFERENCES GENRES (ID)
);


CREATE TABLE COMMENTS
(
    ID          BIGSERIAL    NOT NULL PRIMARY KEY,
    BOOK_ID     BIGINT    NOT NULL,
    AUTHOR_NAME VARCHAR(255) NOT NULL DEFAULT 'ANON',
    COMMENT     VARCHAR(255) NOT NULL,

    CONSTRAINT fk_books_comments
        FOREIGN KEY (BOOK_ID)
            REFERENCES BOOKS (id)
);