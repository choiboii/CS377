-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;


DROP VIEW IF EXISTS SameSongWriterArtistAlbums CASCADE;

-- Create table to albums in which there was at least one song with a song not written by the artist
CREATE VIEW SameSongWriterArtistAlbums as
(SELECT Artist.name as artist_name, Album.title as album_name 
FROM Album, Artist, Song, BelongsToAlbum
WHERE Album.artist_id = Artist.artist_id AND Album.album_id = BelongsToAlbum.album_id AND Song.song_id = BelongsToAlbum.song_id AND Album.artist_id = Song.songwriter_id)
EXCEPT
(SELECT Artist.name as artist_name, Album.title as album_name
FROM Album, Artist, Song, BelongsToAlbum
WHERE Album.artist_id = Artist.artist_id AND Album.album_id = BelongsToAlbum.album_id AND Song.song_id = BelongsToAlbum.song_id AND Album.artist_id <> Song.songwriter_id);

-- Answer:
SELECT DISTINCT artist_name, album_name
FROM SameSongWriterArtistAlbums
ORDER BY artist_name ASC, album_name ASC;

DROP VIEW IF EXISTS SameSongWriterArtistAlbums CASCADE;