CREATE TABLE IF NOT EXISTS RATING_MPA
(
    RATING_ID   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    RATING_NAME   VARCHAR(10)  NOT NULL
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR(50)  NOT NULL,
    UNIQUE(GENRE_ID, NAME)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INT PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR(255)  NOT NULL,
    RELEASE_DATE DATE  NOT NULL,
    DESCRIPTION  VARCHAR(500),
    DURATION     INT,
    RATE         INT,
    RATING       INT NOT NULL REFERENCES RATING_MPA(RATING_ID)
);

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID INT PRIMARY KEY AUTO_INCREMENT,
    LOGIN       VARCHAR(255)  NOT NULL,
    NAME        VARCHAR(255),
    EMAIL       VARCHAR(255) NOT NULL,
    BIRTHDAY    DATE
);

CREATE TABLE IF NOT EXISTS FAVORITE_FILMS
(
    FILM_ID     INT REFERENCES FILMS(FILM_ID),
    USER_ID     INT REFERENCES USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS USER_FRIEND
(
    USER_ID     INT NOT NULL REFERENCES USERS(USER_ID),
    FRIEND_ID   INT NOT NULL REFERENCES USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID     INT REFERENCES FILMS(FILM_ID) ON UPDATE cascade ,
    GENRE_ID    INT REFERENCES GENRES(GENRE_ID) ON UPDATE cascade
);

