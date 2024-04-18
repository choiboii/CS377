-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;

DELETE FROM ProducedBy
WHERE EXISTS (
SELECT *
FROM ProducedBy JOIN Album ON ProducedBy.album_id = Album.album_id
WHERE Album.title = 'Thriller');

DELETE FROM Collaboration
WHERE EXISTS (
SELECT *
FROM Album JOIN BelongsToAlbum ON Album.album_id = BelongsToAlbum.album_id JOIN Song On BelongsToAlbum.song_id = Song.song_id JOIN Collaboration ON Song.song_id = Collaboration.song_id
WHERE Album.title = 'Thriller');

DELETE FROM BelongsToAlbum
WHERE EXISTS (
SELECT Song.song_id
FROM Album JOIN BelongsToAlbum ON Album.album_id = BelongsToAlbum.album_id JOIN Song On BelongsToAlbum.song_id = Song.song_id
WHERE Album.title = 'Thriller');

DELETE FROM Album
WHERE Album.title = 'Thriller';