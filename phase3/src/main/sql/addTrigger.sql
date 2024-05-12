create or replace trigger add_owner_to_session after insert on session
for each row
  execute function add_owner_to_session();

drop trigger add_owner_to_session on session;