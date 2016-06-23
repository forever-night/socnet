create table role (
  id serial primary key,
  title varchar(20)
);


alter table account
  add column role_id int not null,
  add constraint account_role_fkey foreign key (role_id)
    references role(id),
  drop column if exists salt;