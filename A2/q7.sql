-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;

-- Add collaboration info for Justin Bieber's song:
INSERT INTO Collaboration Values
((SELECT song_id FROM Song WHERE Song.title = 'Never Say Never'),
(SELECT artist_id FROM Artist WHERE Artist.name = 'Justin Bieber'),
(SELECT Artist.artist_id FROM Artist, Album, BelongsToAlbum, Song
WHERE Artist.artist_id = Album.artist_id AND Album.album_id = BelongsToAlbum.album_id AND BelongsToAlbum.song_id = Song.song_id AND Song.title = 'Close Your Eyes'));

-- Add collboration info for the other artist's song:
INSERT INTO Collaboration Values
((SELECT song_id FROM Song WHERE Song.title = 'Close Your Eyes'),
(SELECT Artist.artist_id FROM Artist, Album, BelongsToAlbum, Song
WHERE Artist.artist_id = Album.artist_id AND Album.album_id = BelongsToAlbum.album_id AND BelongsToAlbum.song_id = Song.song_id AND Song.title = 'Close Your Eyes'),
(SELECT artist_id FROM Artist WHERE Artist.name = 'Justin Bieber'));
