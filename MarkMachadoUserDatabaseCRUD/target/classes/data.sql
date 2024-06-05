INSERT INTO TestObject(name,age,birthday) VALUES
('John Doe', 25, '1998-05-15'),
('Alice Smith', 30, '1993-10-22'),
('Michael Johnson', 28, '1995-12-07');

INSERT INTO sec_user (firstName, lastName, email, encryptedPassword, enabled)
VALUES ('Mark', 'Machado', 'manager@pollapp.com', '$2a$10$87IYNjcCfwsX9fuinrYEv.MXURvN7LX0s9j3sRDjOgCzzH1DmIaxe', 1);
 
INSERT INTO sec_role (roleName)
VALUES ('ROLE_ADMIN');
 
INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');
 
INSERT INTO user_role (userId, roleId)
VALUES (1, 1);
 