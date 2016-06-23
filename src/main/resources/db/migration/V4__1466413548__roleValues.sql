insert into role(title) values ('admin');
insert into role(title) values ('user');


update account set role_id = (
  SELECT r.id
  FROM role r
  WHERE r.title = 'user'
)
  where role_id is null;