-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;


DROP VIEW IF EXISTS GenreSales CASCADE;
DROP VIEW IF EXISTS LeastPopularGenre CASCADE;

-- Create table to find relationship between genre and sales
CREATE VIEW GenreSales AS
SELECT Genre.genre_id, Genre.genre, sum(Album.sales)
FROM Genre, Album
WHERE Genre.genre_id = Album.genre_id
GROUP BY Genre.genre_id, Genre.genre;

-- Create table to find the least popular genre based on sales
CREATE VIEW LeastPopularGenre AS
SELECT GenreSales.genre_id, GenreSales.genre
FROM GenreSales
WHERE GenreSales.sum <= (SELECT min(GenreSales.sum) FROM GenreSales);

-- Answer:
(SELECT Artist.name as musician, LeastPopularGenre.genre as genre
FROM Artist, Album, LeastPopularGenre
WHERE Artist.artist_id = Album.artist_id AND Album.genre_id = LeastPopularGenre.genre_id)
EXCEPT
(SELECT Artist.name as musician, LeastPopularGenre.genre as genre
FROM Artist, Album, LeastPopularGenre
WHERE Artist.artist_id = Album.artist_id AND Album.genre_id <> LeastPopularGenre.genre_id
ORDER BY Artist.name ASC);

DROP VIEW IF EXISTS LeastPopularGenre CASCADE;
DROP VIEW IF EXISTS GenreSales CASCADE;