-- Dedicated sequence for account numbers, kept separate from the technical PK.
create sequence if not exists account_number_seq start with 1 increment by 1;
