USE ejd;
INSERT INTO poll(title,question,answer1,answer2,answer3,votes1,votes2,votes3,datetaken) VALUES
('Gender', 'What is your gender?', 'Male', 'Female', 'Other', 40, 30, 2, '2023-10-14'),
('Holiday', 'What is your dream holiday spot?', 'Haiwaii', 'Japan', 'Home!', 105, 209, 78, '2020-03-15'),
('Videos', 'How do you view videos?', 'Computer', 'Laptop', 'Mobile Device', 50, 20, 75, '2021-11-20'),
('Music', 'What music service do you use?', 'Spotify', 'Apple Music', 'Other', 100, 64, 33, '2019-01-03');

INSERT INTO sec_user (firstName, lastName, email, encryptedPassword, enabled)
VALUES ('Mark', 'Machado', 'manager@pollapp.com', '$2a$10$87IYNjcCfwsX9fuinrYEv.MXURvN7LX0s9j3sRDjOgCzzH1DmIaxe', 1);
 
INSERT INTO sec_role (roleName)
VALUES ('ROLE_ADMIN');
 
INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');
 
INSERT INTO user_role (userId, roleId)
VALUES (1, 1);
 