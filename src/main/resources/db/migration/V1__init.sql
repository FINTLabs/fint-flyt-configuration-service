create sequence hibernate_sequence start 1 increment 1;
create table authentication_subject_revision_entity
(
    id        int4 not null,
    timestamp int8 not null,
    subject   varchar(255),
    primary key (id)
);
create table configuration
(
    id                      uuid    not null,
    comment                 varchar(255),
    completed               boolean not null,
    integration_id          varchar(255),
    integration_metadata_id varchar(255),
    version                 int4,
    primary key (id)
);
create table configuration_aud
(
    id                          uuid not null,
    rev                         int4 not null,
    revtype                     int2,
    comment                     varchar(255),
    comment_mod                 boolean,
    completed                   boolean,
    completed_mod               boolean,
    integration_id              varchar(255),
    integration_id_mod          boolean,
    integration_metadata_id     varchar(255),
    integration_metadata_id_mod boolean,
    version                     int4,
    version_mod                 boolean,
    elements_mod                boolean,
    primary key (id, rev)
);
create table configuration_configuration_element_aud
(
    rev              int4 not null,
    configuration_id uuid not null,
    id               uuid not null,
    revtype          int2,
    primary key (rev, configuration_id, id)
);
create table configuration_element_aud
(
    id                       uuid not null,
    rev                      int4 not null,
    revtype                  int2,
    key                      varchar(255),
    key_mod                  boolean,
    elements_mod             boolean,
    field_configurations_mod boolean,
    primary key (id, rev)
);
create table configuration_element
(
    id                              uuid not null,
    key                             varchar(255),
    parent_configuration_element_id uuid,
    configuration_id                uuid,
    primary key (id)
);
create table configuration_element_configuration_element_aud
(
    rev                             int4 not null,
    parent_configuration_element_id uuid not null,
    id                              uuid not null,
    revtype                         int2,
    primary key (rev, parent_configuration_element_id, id)
);
create table configuration_element_field_configuration_aud
(
    rev                      int4 not null,
    configuration_element_id uuid not null,
    id                       uuid not null,
    revtype                  int2,
    primary key (rev, configuration_element_id, id)
);
create table field_collection_configuration_aud
(
    id         uuid not null,
    rev        int4 not null,
    revtype    int2,
    key        varchar(255),
    key_mod    boolean,
    type       varchar(255),
    type_mod   boolean,
    values_mod boolean,
    primary key (id, rev)
);
create table field_collection_configuration_values_aud
(
    rev                               int4         not null,
    field_collection_configuration_id uuid         not null,
    values                            varchar(255) not null,
    revtype                           int2,
    primary key (rev, field_collection_configuration_id, values)
);
create table field_configuration_aud
(
    id        uuid not null,
    rev       int4 not null,
    revtype   int2,
    key       varchar(255),
    key_mod   boolean,
    type      varchar(255),
    type_mod  boolean,
    value     varchar(255),
    value_mod boolean,
    primary key (id, rev)
);
create table field_collection_configuration
(
    id   uuid not null,
    key  varchar(255),
    type varchar(255),
    primary key (id)
);
create table field_collection_configuration_values
(
    field_collection_configuration_id uuid not null,
    values                            varchar(255)
);
create table field_configuration
(
    id                       uuid not null,
    key                      varchar(255),
    type                     varchar(255),
    value                    varchar(255),
    configuration_element_id uuid,
    primary key (id)
);
alter table configuration_aud
    add constraint FKlrjtdjv66vmobnkcbdejmhxj5 foreign key (rev) references authentication_subject_revision_entity;
alter table configuration_configuration_element_aud
    add constraint FK3n0fwqjl9jdlt4r3wdpvec2wv foreign key (rev) references authentication_subject_revision_entity;
alter table configuration_element_aud
    add constraint FKq01u0h0yc0t8dot5e1suplv6t foreign key (rev) references authentication_subject_revision_entity;
alter table configuration_element
    add constraint FKabf8o27e4gdkph3ywbv7moyc foreign key (parent_configuration_element_id) references configuration_element;
alter table configuration_element
    add constraint FKhsvcfe0oq47x078sggh4gg2va foreign key (configuration_id) references configuration;
alter table configuration_element_configuration_element_aud
    add constraint FK8a5mrstm0k41byojiwk4q3qhk foreign key (rev) references authentication_subject_revision_entity;
alter table configuration_element_field_configuration_aud
    add constraint FKqxp8r56duxhpgsua0au42kk8e foreign key (rev) references authentication_subject_revision_entity;
alter table field_collection_configuration_aud
    add constraint FKlryf7mcd1xcnlqwyhnwe6mbqh foreign key (rev) references authentication_subject_revision_entity;
alter table field_collection_configuration_values_aud
    add constraint FK1n6p99hkh8ppmwu8g0nu1jc6o foreign key (rev) references authentication_subject_revision_entity;
alter table field_configuration_aud
    add constraint FK5a83d462ymajk7cehrt2sb3b4 foreign key (rev) references authentication_subject_revision_entity;
alter table field_collection_configuration_values
    add constraint FK8w97f2ngpyyyn3x75b3km37e8 foreign key (field_collection_configuration_id) references field_collection_configuration;
alter table field_configuration
    add constraint FKcvwi1c92qqkoxofb6ygdd65pq foreign key (configuration_element_id) references configuration_element;
