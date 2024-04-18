-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;


DROP VIEW IF EXISTS CollabAlbums CASCADE;
DROP VIEW IF EXISTS NoCollabAlbums CASCADE;
DROP VIEW IF EXISTS CollabAlbumSales CASCADE;
DROP VIEW IF EXISTS NoCollabAlbumSales CASCADE;

-- Create table for albums with collaborations
CREATE VIEW CollabAlbums as
SELECT DISTINCT Collaboration.artist1, Album.album_id, Album.sales
FROM Album, Song, BelongsToAlbum, Collaboration
WHERE Album.album_id = BelongsToAlbum.album_id AND Song.song_id = BelongsToAlbum.song_id AND Song.song_id = Collaboration.song_id;

-- Create table for albums without collaborations for those same artists
CREATE VIEW NoCollabAlbums as
SELECT Album.artist_id, Album.album_id, Album.sales
FROM Album, CollabAlbums
WHERE Album.artist_id = CollabAlbums.artist1 AND Album.album_id <> CollabAlbums.album_id;

-- Create table for album sales with collborators
CREATE VIEW CollabAlbumSales as
SELECT CollabAlbums.artist1, avg(CollabAlbums.sales)
FROM CollabAlbums
GROUP BY CollabAlbums.artist1;

-- Create table for album sales without collborators

CREATE VIEW NoCollabAlbumSales as
SELECT NoCollabAlbums.artist_id, avg(NoCollabAlbums.sales)
FROM NoCollabAlbums
GROUP BY NoCollabAlbums.artist_id;

-- Answer:
SELECT Artist.name as artists, CollabAlbumSales.avg as avg_collab_sales
FROM Artist, CollabAlbumSales, NoCollabAlbumSales
WHERE Artist.artist_id = CollabAlbumSales.artist1 AND CollabAlbumSales.artist1 = NoCollabAlbumSales.artist_id AND CollabAlbumSales.avg > NoCollabAlbumSales.avg
ORDER BY Artist.name ASC;

DROP VIEW IF EXISTS NoCollabAlbumSales CASCADE;
DROP VIEW IF EXISTS CollabAlbumSales CASCADE;
DROP VIEW IF EXISTS NoCollabAlbums CASCADE;
DROP VIEW IF EXISTS CollabAlbums CASCADE;