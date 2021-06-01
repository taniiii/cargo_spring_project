delete from transportations;

insert into transportations(id, comment, user_id, tariff_id, status, created_at, delivery_at) values
(1, 'France shipping', 1, 2, 'NEW', '2021-05-10', '2021-05-14'),
(2, 'Italy shipping', 1, 5, 'PAID', '2021-05-11', '2021-05-15'),
(3, 'Ireland shipping', 1, 10, 'WAITING_FOR_PAYMENT', '2021-05-09', '2021-05-13'),
(4, 'France shipping', 2, 8, 'REJECTED', '2021-05-10', '2021-05-14');

alter table hibernate_sequence auto_increment = 10;