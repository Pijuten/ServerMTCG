CREATE TABLE userData(
                         username VARCHAR PRIMARY KEY UNIQUE,--1
                         password bytea,--2
                         token varchar,--3
                         currency int DEFAULT 40,--4
                         score int DEFAULT 1000,--5
                         wins int DEFAULT 0,--6
                         draws int DEFAULT 0,--7
                         losses int DEFAULT 0,--8
                         displayName varchar,--9
                         bio varchar,--10
                         profileimage varchar,--11
                         salt bytea
);
CREATE TABLE cards(
                      cardid varchar PRIMARY KEY UNIQUE,
                      cardname varchar,
                      damage FLOAT,
                      deck BOOLEAN DEFAULT FALSE,
                      username varchar,
                      packageid int,
                      CONSTRAINT FK_username_userData FOREIGN KEY(username)
                          REFERENCES userData(username)
);