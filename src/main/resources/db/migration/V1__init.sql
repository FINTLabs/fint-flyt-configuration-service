create table configuration
(
    id                      bigserial not null,
    comment                 varchar(255),
    completed               boolean   not null,
    integration_id          int8      not null,
    integration_metadata_id int8      not null,
    version                 int4,
    mapping_id              int8      not null,
    primary key (id)
);
create table instance_collection_reference
(
    id                                        bigserial not null,
    index                                     int4      not null,
    reference                                 varchar(255),
    instance_collection_references_ordered_id int8,
    primary key (id)
);
create table object_collection_mapping
(
    id bigserial not null,
    primary key (id)
);
create table object_collection_mapping_object_mappings
(
    parent_object_collection_mapping_id int8 not null,
    child_object_mapping_id             int8 not null
);
create table object_collection_mapping_objects_from_collection_mappings
(
    parent_object_collection_mapping_id      int8 not null,
    child_objects_from_collection_mapping_id int8 not null
);
create table object_mapping
(
    id bigserial not null,
    primary key (id)
);
create table object_mapping_object_collection_mapping_per_key
(
    parent_object_mapping_id           int8         not null,
    child_object_collection_mapping_id int8         not null,
    key                                varchar(255) not null,
    primary key (parent_object_mapping_id, key)
);
create table object_mapping_object_mapping_per_key
(
    parent_object_mapping_id int8         not null,
    child_object_mapping_id  int8         not null,
    key                      varchar(255) not null,
    primary key (parent_object_mapping_id, key)
);
create table object_mapping_value_collection_mapping_per_key
(
    parent_value_mapping_id           int8         not null,
    child_value_collection_mapping_id int8         not null,
    key                               varchar(255) not null,
    primary key (parent_value_mapping_id, key)
);
create table object_mapping_value_mapping_per_key
(
    parent_object_mapping_id int8         not null,
    child_value_mapping_id   int8         not null,
    key                      varchar(255) not null,
    primary key (parent_object_mapping_id, key)
);
create table objects_from_collection_mapping
(
    id                bigserial not null,
    object_mapping_id int8      not null,
    primary key (id)
);
create table value_collection_mapping
(
    id bigserial not null,
    primary key (id)
);
create table value_collection_mapping_value_mappings
(
    parent_value_collection_mapping_id int8 not null,
    child_value_mapping_id             int8 not null
);
create table value_collection_mapping_values_from_collection_mappings
(
    parent_value_collection_mapping_id      int8 not null,
    child_values_from_collection_mapping_id int8 not null
);
create table value_mapping
(
    id             bigserial    not null,
    mapping_string varchar(255),
    type           varchar(255) not null,
    primary key (id)
);
create table values_from_collection_mapping
(
    id               bigserial not null,
    value_mapping_id int8      not null,
    primary key (id)
);
alter table configuration
    add constraint UniqueIntegrationIdAndVersion unique (integration_id, version);
alter table object_collection_mapping_object_mappings
    add constraint UK_hw61w0urgwdicl501c5e2lyhp unique (child_object_mapping_id);
alter table object_collection_mapping_objects_from_collection_mappings
    add constraint UK_6hjjirin84towy8p278k282ti unique (child_objects_from_collection_mapping_id);
alter table object_mapping_object_collection_mapping_per_key
    add constraint UK_8lccajly2mjo40i6562qoucjo unique (child_object_collection_mapping_id);
alter table object_mapping_object_mapping_per_key
    add constraint UK_swx9kysqc7x1x26r3754xrqj0 unique (child_object_mapping_id);
alter table object_mapping_value_collection_mapping_per_key
    add constraint UK_kjbr64ber6mo9s77toni90b5d unique (child_value_collection_mapping_id);
alter table object_mapping_value_mapping_per_key
    add constraint UK_ojbeycjihfhwe514r9wgghk15 unique (child_value_mapping_id);
alter table value_collection_mapping_value_mappings
    add constraint UK_grl7yj4cl7ic1ecpbng58uf6k unique (child_value_mapping_id);
alter table value_collection_mapping_values_from_collection_mappings
    add constraint UK_7t6x0nuaatsruovohqlvx4ow7 unique (child_values_from_collection_mapping_id);
alter table configuration
    add constraint FKwmivbmg8cyv1hyhr9951xu47 foreign key (mapping_id) references object_mapping;
alter table instance_collection_reference
    add constraint FK6q2d8n31nhwxoe2pfbl6kru7v foreign key (instance_collection_references_ordered_id) references values_from_collection_mapping;
alter table object_collection_mapping_object_mappings
    add constraint FKtc0hw3c6i60sajpp20rhx8bva foreign key (child_object_mapping_id) references object_mapping;
alter table object_collection_mapping_object_mappings
    add constraint FKhoecu4ipksr1u7eip2yjy8u05 foreign key (parent_object_collection_mapping_id) references object_collection_mapping;
alter table object_collection_mapping_objects_from_collection_mappings
    add constraint FKcjfi0ch34eogjd3nad2rgrf3b foreign key (child_objects_from_collection_mapping_id) references objects_from_collection_mapping;
alter table object_collection_mapping_objects_from_collection_mappings
    add constraint FKanmxyrb8gwaff72cb73pgsgbp foreign key (parent_object_collection_mapping_id) references object_collection_mapping;
alter table object_mapping_object_collection_mapping_per_key
    add constraint FKnd07fnsx7x9do77rcbotsr33c foreign key (child_object_collection_mapping_id) references object_collection_mapping;
alter table object_mapping_object_collection_mapping_per_key
    add constraint FKbvgpllrdya03336vh8drag24c foreign key (parent_object_mapping_id) references object_mapping;
alter table object_mapping_object_mapping_per_key
    add constraint FKgcmm1agarkbq60gwcan0ctnrp foreign key (child_object_mapping_id) references object_mapping;
alter table object_mapping_object_mapping_per_key
    add constraint FK7s674dhtort8l2k42bfhu8q49 foreign key (parent_object_mapping_id) references object_mapping;
alter table object_mapping_value_collection_mapping_per_key
    add constraint FKgdxnmi62aysfe0k36bg1xvj2q foreign key (child_value_collection_mapping_id) references value_collection_mapping;
alter table object_mapping_value_collection_mapping_per_key
    add constraint FK4fma39r6x5jloxtd9rnn3a3lc foreign key (parent_value_mapping_id) references object_mapping;
alter table object_mapping_value_mapping_per_key
    add constraint FKndkecr6l963y10f4a8yjad09c foreign key (child_value_mapping_id) references value_mapping;
alter table object_mapping_value_mapping_per_key
    add constraint FK7mvcn4nmbu8cxhd4bc8bak84b foreign key (parent_object_mapping_id) references object_mapping;
alter table objects_from_collection_mapping
    add constraint FKg9htsc08dvpg63kjxxn2914ds foreign key (object_mapping_id) references object_mapping;
alter table value_collection_mapping_value_mappings
    add constraint FK46h5to8am60buar9at0vr5d67 foreign key (child_value_mapping_id) references value_mapping;
alter table value_collection_mapping_value_mappings
    add constraint FKbfr1w206mk023yvr6ntx0jbaf foreign key (parent_value_collection_mapping_id) references value_collection_mapping;
alter table value_collection_mapping_values_from_collection_mappings
    add constraint FKaw50ggqqbixdxvj7vxcxhae7m foreign key (child_values_from_collection_mapping_id) references values_from_collection_mapping;
alter table value_collection_mapping_values_from_collection_mappings
    add constraint FK4pscj3gyrxht09hysap7bciv5 foreign key (parent_value_collection_mapping_id) references value_collection_mapping;
alter table values_from_collection_mapping
    add constraint FKe99xh6knmllx5olj1rjpj5gcg foreign key (value_mapping_id) references value_mapping;
