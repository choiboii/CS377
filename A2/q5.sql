-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;

DROP VIEW IF EXISTS DebutArtist CASCADE;
DROP VIEW IF EXISTS DebutAlbum CASCADE;
DROP VIEW IF EXISTS CanadianDebutIndieArtists CASCADE;
DROP VIEW IF EXISTS AmericanLabelArtists CASCADE;

-- Create table for an first album
CREATE VIEW DebutArtist as
SELECT Album.artist_id, min(Album.year)
FROM Album
GROUP BY Album.artist_id;

-- Create table to see which first albums are indie
CREATE VIEW DebutAlbum as
SELECT DebutArtist.artist_id, Album.album_id, DebutArtist.min as year                                                                                                                                                                                                                 
FROM DebutArtist, Album
WHERE DebutArtist.artist_id = Album.artist_id;

-- Create table to see which of those debut albums are created by Canadian Indie artists
CREATE VIEW CanadianDebutIndieArtists as
SELECT Artist.name as artist_name
FROM DebutAlbum LEFT JOIN ProducedBy ON DebutAlbum.album_id = ProducedBy.album_id NATURAL JOIN Artist
WHERE Artist.nationality = 'Canada' AND ProducedBy.label_id IS NULL;

-- Create table for any artists signed under an american label
CREATE VIEW AmericanLabelArtists as
Select Artist.name as artist_name
FROM Artist NATURAL JOIN Album NATURAL JOIN ProducedBy NATURAL JOIN RecordLabel
WHERE RecordLabel.country = 'America';

-- Answer:
(SELECT *
FROM CanadianDebutIndieArtists)
INTERSECT
(SELECT *
FROM AmericanLabelArtists
ORDER BY AmericanLabelArtists.artist_name ASC);

DROP VIEW IF EXISTS AmericanLabelArtists CASCADE;
DROP VIEW IF EXISTS CanadianDebutIndieArtists CASCADE;
DROP VIEW IF EXISTS DebutAlbum CASCADE;
DROP VIEW IF EXISTS DebutArtist CASCADE;


