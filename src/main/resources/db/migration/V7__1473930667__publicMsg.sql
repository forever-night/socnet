alter table public_message
  drop constraint public_message_receiver_id_fkey,
  drop column receiver_id;