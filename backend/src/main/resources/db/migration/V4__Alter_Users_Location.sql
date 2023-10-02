ALTER TABLE users
DROP COLUMN location;

ALTER TABLE users
ADD COLUMN location_id SERIAL REFERENCES Locations(location_id);