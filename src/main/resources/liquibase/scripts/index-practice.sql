-- liquibase formatted sql

-- changeset bspopov:1
create index chats_username_index on chats (username);

-- changeset bspopov:2
create  index messages_date_time_index on messages (date_time);