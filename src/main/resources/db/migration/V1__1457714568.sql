create table account (
  id serial primary key,
  login varchar(24),
  email varchar(128),
  password varchar(128),
  salt varchar(128),
  private_key varchar(64),
  public_key varchar(64),
  created_at timestamptz
);


create table profile (
  id int primary key,
  name varchar(128),
  country varchar(64),
  current_city varchar(64),
  phone varchar(16),
  birth_date date,
  info text,
  foreign key (id) references account(id)
);


create table profile_follower (
  owner int references profile(id),
  follower int references profile(id)
);


create table private_message (
  id serial primary key,
  sender int references profile(id),
  receiver int references profile(id),
  text_content text,
  created_at timestamptz
);


create table public_message (
  id serial primary key,
  sender int references profile(id),
  text_content text,
  profile_location int references profile(id),
  created_at timestamptz
);


create table community (
  id serial primary key,
  title varchar(128),
  owner int references profile(id),
  created_at timestamptz
);


create table community_admin (
  community_id int references community(id),
  admin_id int references profile(id)
);


create table community_follower (
  owner int references community(id),
  follower int references profile(id)
);


create table community_message (
  id serial primary key,
  sender int references community(id),
  text_content text,
  created_at timestamptz
);


create table reply_message (
  id serial primary key,
  sender int references profile(id),
  text_content text,
  reply_to_profile int references public_message(id),
  reply_to_community int references community_message(id),
  created_at timestamptz
);