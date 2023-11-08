use
    second_hand;
create table category
(
    category_id bigint auto_increment primary key,
    name        varchar(45)  not null,
    img_url     varchar(200) not null,
    placeholder varchar(300) not null
);

create table town
(
    town_id  bigint primary key,
    city     varchar(45) not null,
    county   varchar(45) not null,
    district varchar(45) not null
);

create table member
(
    member_id      bigint auto_increment
        primary key,
    login_name     varchar(45)  not null,
    member_email   varchar(45)  not null unique,
    member_token   varchar(500) null,
    img_url        varchar(200) null,
    main_town_id   bigint       null,
    sub_town_id    bigint       null,
    oauth_provider varchar(45)  not null,
    constraint fk_member_town1
        foreign key (main_town_id) references town (town_id),
    constraint fk_member_town2
        foreign key (sub_town_id) references town (town_id)
);


create table product
(
    product_id    bigint auto_increment
        primary key,
    title         varchar(45)  not null,
    content       text         not null,
    price         int          null,
    status        varchar(45)  not null default 'SELLING',
    created_at    datetime     NULL     DEFAULT CURRENT_TIMESTAMP,
    count_view    smallint              default 0,
    count_like    smallint              default 0,
    chat_count    INT          NOT NULL DEFAULT 0 COMMENT '채팅수',
    thumbnail_url varchar(200) not null,
    town_id       bigint       not null,
    category_id   bigint       not null,
    member_id     bigint       not null,
    deleted       tinyint(1)   not null default 0,
    constraint fk_product_category1
        foreign key (category_id) references category (category_id),
    constraint fk_product_member1
        foreign key (member_id) references member (member_id),
    constraint fk_product_town1
        foreign key (town_id) references town (town_id)
);
-- product 외래키들 index로 변경


create table interested
(
    interested_id bigint auto_increment primary key,
    product_id    bigint     not null,
    member_id     bigint     not null,
    is_liked      tinyint(1) not null,
    constraint fk_interested_member1
        foreign key (member_id) references member (member_id),
    constraint fk_interested_product1
        foreign key (product_id) references product (product_id)
);

create table product_img
(
    product_img_id bigint auto_increment primary key,
    img_url        varchar(200) not null,
    product_id     bigint       null,
    constraint fk_product_img_product
        foreign key (product_id) references product (product_id)
);

create table room
(
    room_id    bigint auto_increment primary key,
    title      varchar(45) not null,
    created_at datetime    NULL DEFAULT CURRENT_TIMESTAMP
);

create table participant_room
(
    participant_room bigint auto_increment primary key,
    member_id        bigint not null,
    room_id          bigint not null,
    constraint fk_member_id
        foreign key (member_id) references member (member_id),
    constraint fk_room_id
        foreign key (room_id) references room (room_id)
);

create table chat_room
(
    chat_room_id   bigint auto_increment
        primary key,
    title          varchar(45)   null,
    created_at     datetime      not null,
    last_send_time datetime      not null,
    contents       text          null,
    subject        varchar(1000) not null,
    product_id     bigint        not null,
    seller_id      bigint        not null,
    customer_id    bigint        not null,
    constraint fk_chat_room_member1
        foreign key (seller_id) references member (member_id),
    constraint fk_chat_room_member2
        foreign key (customer_id) references member (member_id),
    constraint fk_chat_room_product1
        foreign key (product_id) references product (product_id)
);

create table chat_log
(
    id           bigint auto_increment primary key,
    message      varchar(1000) not null,
    read_count   bigint        not null,
    sender_id    bigint        not null,
    createdAt    datetime      not null,
    chat_room_id bigint        not null,
    constraint fk_chat_room_id
        foreign key (chat_room_id) references chat_room (chat_room_id)
);


CREATE TABLE IF NOT EXISTS chat_room
(
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    created_at     TIMESTAMP     NOT NULL,
    buyer_id       BIGINT        NOT NULL,
    seller_id      BIGINT        NOT NULL,
    item_id        BIGINT        NOT NULL,
    subject        VARCHAR(1000) NOT NULL COMMENT '마지막으로 보낸 메시지',
    last_send_time TIMESTAMP     NULL COMMENT '마지막 메시지 전송 시간',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS chat_log
(
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    message      VARCHAR(1000) NOT NULL,
    read_count   INT           NOT NULL COMMENT '메시지를 읽지 않은 사람의 수',
    sender_id    BIGINT        NOT NULL COMMENT '메시지를 전송한 사람의 PK',
    created_at   TIMESTAMP     NOT NULL COMMENT '메시지 전송 시간',
    chat_room_id BIGINT        NOT NULL,
    PRIMARY KEY (id)
);



CREATE TABLE member_token
(
    member_id    BIGINT       NOT NULL,
    member_token VARCHAR(256) NOT NULL,
    PRIMARY KEY (member_id)
)

