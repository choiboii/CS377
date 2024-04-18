-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;


DROP VIEW IF EXISTS DuplicateSongs CASCADE;

-- Create a view for all duplicate songs
CREATE VIEW DuplicateSongs as
SELECT b1.song_id, b1.album_id
FROM BelongsToAlbum b1
WHERE EXISTS (
SELECT *
FROM BelongsToAlbum
WHERE BelongsToAlbum.album_id <> b1.album_id AND BelongsToAlbum.song_id = b1.song_id);

-- Answer:
SELECT Song.title as song_name, Album.year as year, Artist.name as artist_name
FROM Artist, Album, Song, DuplicateSongs
WHERE Artist.artist_id = Album.artist_id AND Album.album_id = DuplicateSongs.album_id AND Song.song_id = DuplicateSongs.song_id
ORDER BY song_name ASC, year ASC, artist_name ASC;

DROP VIEW IF EXISTS DuplicateSongs CASCADE;
