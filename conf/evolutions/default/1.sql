# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table evaluations (
  id                        bigint not null,
  q1                        varchar(255),
  q2                        varchar(255),
  q3                        varchar(255),
  constraint pk_evaluations primary key (id))
;

create table users (
  email                     varchar(255) not null,
  name                      varchar(255),
  surname                   varchar(255),
  password                  varchar(255),
  constraint pk_users primary key (email))
;

create sequence evaluations_id_seq;

create sequence users_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists evaluations;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists evaluations_id_seq;

drop sequence if exists users_seq;

