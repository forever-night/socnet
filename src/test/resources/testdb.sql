set mode PostgreSQL;

create table public.account (
  id serial primary key,
  login varchar(24),
  email varchar(128),
  password varchar(128),
  created_at timestamp,
  version int,
  lower_login varchar(24) as lower(login),
  role varchar(16),
  enabled boolean not null default true
);


create table public.profile (
  account_id int primary key,
  name varchar(128),
  country varchar(64),
  current_city varchar(64),
  phone varchar(16),
  birth_date date,
  info varchar,
  version int,
  foreign key (account_id) references account(id)
);


create table public.profile_follower (
  owner_id int references profile(account_id),
  follower_id int references profile(account_id)
);


create table public.private_message (
  id serial primary key,
  sender_id int references profile(account_id),
  receiver_id int references profile(account_id),
  text_content varchar,
  created_at timestamp
);


create table public.public_message (
  id serial primary key,
  sender_id int references profile(account_id),
  text_content varchar,
  receiver_id int references profile(account_id),
  created_at timestamp,
  version int
);


create table public.community (
  id serial primary key,
  title varchar(128),
  owner_id int references profile(account_id),
  created_at timestamp,
  version int
);


create table public.community_admin (
  community_id int references community(id),
  admin_id int references profile(account_id)
);


create table public.community_follower (
  owner_id int references community(id),
  follower_id int references profile(account_id)
);


create table public.community_message (
  id serial primary key,
  sender_id int references community(id),
  text_content varchar,
  created_at timestamp,
  version int
);

create unique index on public.account (lower_login);