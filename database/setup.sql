DROP DATABASE IF EXISTS testdb;
CREATE DATABASE testdb;
CREATE OR REPLACE TABLE testdb.Highscore(
    score_id INT NOT NULL AUTO_INCREMENT,
    player_name VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    # maybe GameMode after we finished our mvp
    submission_date DATE DEFAULT CURDATE(),
    Primary Key ( score_id )
);