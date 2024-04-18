-- THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING 
-- A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - ANDREW CHOI

SET search_path TO artistdb;


DROP VIEW IF EXISTS DebutYearSteppenwolf CASCADE;

-- Create table to find year of Steppenwolf's debut album
CREATE VIEW DebutYearSteppenwolf as
SELECT min(SteppenAlbum.year)
FROM (Select Album.year 
FROM Album natural join Artist 
WHERE Artist.name = 'Steppenwolf')SteppenAlbum;

-- Answer:
SELECT DISTINCT Artist.name, Artist.nationality
FROM Artist, DebutYearSteppenwolf, Role
WHERE Extract(Year from Artist.birthdate) = DebutYearSteppenwolf.min AND Role.artist_id = Artist.Artist_id AND Role.role <> 'Band'
ORDER BY Artist.name ASC;

DROP VIEW IF EXISTS DebutYearSteppenwolf CASCADE;