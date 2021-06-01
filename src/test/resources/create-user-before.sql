use cargo_db_test;
delete from user_role;
delete from users;

insert into users (id, active, email, password, username) values
(1, true, 'user@google.com', '$2a$08$A8T8QwLeD9.A3jInd7/iAOqM/iC4t0.tAbGXw..8MZeWoiTQhPyI2', 'user'),
(2, true, 'test@google.com', '$2a$08$A8T8QwLeD9.A3jInd7/iAOqM/iC4t0.tAbGXw..8MZeWoiTQhPyI2', 'test');

insert into user_role (user_id, roles) values
(1, 'USER'),
(2, 'ADMIN');