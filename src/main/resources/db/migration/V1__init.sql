create table configuration
(
    id                      bigserial not null,
    comment                 varchar(255),
    completed               boolean   not null,
    integration_id          int8      not null,
    integration_metadata_id int8      not null,
    version                 int4,
    primary key (id)
);
create table configuration_element
(
    id                              bigserial not null,
    key                             varchar(255),
    parent_configuration_element_id int8,
    configuration_id                int8,
    primary key (id)
);
create table field_collection_configuration
(
    id                       bigserial    not null,
    key                      varchar(255),
    type                     varchar(255) not null,
    configuration_element_id int8,
    primary key (id)
);
create table field_collection_configuration_values
(
    field_collection_configuration_id int8 not null,
    values                            varchar(255)
);
create table field_configuration
(
    id                       bigserial    not null,
    key                      varchar(255),
    type                     varchar(255) not null,
    value                    varchar(255),
    configuration_element_id int8,
    primary key (id)
);
alter table configuration_element
    add constraint FKabf8o27e4gdkph3ywbv7moyc foreign key (parent_configuration_element_id) references configuration_element;
alter table configuration_element
    add constraint FKhsvcfe0oq47x078sggh4gg2va foreign key (configuration_id) references configuration;
alter table field_collection_configuration
    add constraint FKe2fc7roqyqeelpxx1dycf3d2m foreign key (configuration_element_id) references configuration_element;
alter table field_collection_configuration_values
    add constraint FK8w97f2ngpyyyn3x75b3km37e8 foreign key (field_collection_configuration_id) references field_collection_configuration;
alter table field_configuration
    add constraint FKcvwi1c92qqkoxofb6ygdd65pq foreign key (configuration_element_id) references configuration_element;
