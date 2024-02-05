insert into member (email, password, name, nickname, phone_num, profile_image, authority, provider, activated, created_date, updated_date)
values ('admin', '$2a$12$PDXb2WLf8.tfAORXLxjFw.K83hD9FZnVUwSMGrJTnyGgq.tR0eiYm', 'adminName', 'adminNickname', '01011111111', 'image', 'ROLE_ADMIN', 'DEFAULT', true, now(), now());
