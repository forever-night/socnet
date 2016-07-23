create table account (
  id serial primary key,
  login varchar(24),
  email varchar(128),
  password varchar(128),
  created_at timestamptz,
  version int
);


create table profile (
  id int primary key,
  name varchar(128),
  country varchar(64),
  current_city varchar(64),
  phone varchar(16),
  birth_date date,
  info text,
  version int,
  foreign key (id) references account(id)
);


create table profile_follower (
  owner_id int references profile(id),
  follower_id int references profile(id)
);


create table private_message (
  id serial primary key,
  sender_id int references profile(id),
  receiver_id int references profile(id),
  text_content text,
  created_at timestamptz
);


create table public_message (
  id serial primary key,
  sender_id int references profile(id),
  text_content text,
  receiver_id int references profile(id),
  created_at timestamptz,
  version int
);


create table community (
  id serial primary key,
  title varchar(128),
  owner_id int references profile(id),
  created_at timestamptz,
  version int
);


create table community_admin (
  community_id int references community(id),
  admin_id int references profile(id)
);


create table community_follower (
  owner_id int references community(id),
  follower_id int references profile(id)
);


create table community_message (
  id serial primary key,
  sender_id int references community(id),
  text_content text,
  created_at timestamptz,
  version int
);