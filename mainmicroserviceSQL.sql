
CREATE TABLE Services
(
	ServiceWebSite CHARACTER VARYING(30) PRIMARY KEY,
	ServiceName CHARACTER VARYING(30) UNIQUE  NOT NULL
);

INSERT INTO Services (ServiceName, ServiceWebSite) 
VALUES 
('ownservice','www.localhost:8080'),
('google','www.google.com'),
('facebook','www.facebook.com'),
('vk','www.vk.com');

CREATE TABLE Users
(
    UserId 	SERIAL PRIMARY KEY,
    FirstName CHARACTER VARYING(30),
    LastName CHARACTER VARYING(30),
    Email CHARACTER VARYING(30),
	Password CHARACTER VARYING(30),
	ServiceWebSite CHARACTER VARYING(30),
    Age INTEGER,
	FOREIGN KEY (ServiceWebSite) REFERENCES Services (ServiceWebSite) ON DELETE CASCADE
);

INSERT INTO Users (FirstName, LastName,Email,Password,ServiceWebSite ,Age) 
VALUES 
('Armen','Tovmasyan','armentovmasyan02@gmail.com','tovmasyan','www.facebook.com','20');
