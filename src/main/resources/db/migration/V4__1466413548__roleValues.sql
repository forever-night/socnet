insert into role(title) values ('admin');
insert into role(title) values ('user');


create or replace function get_user_role_id()
  returns int language sql as
  $$
  select r.id from role r
  where r.title = 'user';
  $$;


update account set role_id = get_user_role_id()
  where role_id is null;

alter table account
  alter column role_id set default get_user_role_id(),
  alter column role_id set not null;