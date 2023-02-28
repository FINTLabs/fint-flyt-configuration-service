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
create table object_collection_mapping_element_mappings
(
    collection_mapping_id int8 not null,
    element_mapping_id    int8 not null
);
create table object_collection_mapping_from_collection_mappings
(
    collection_mapping_id      int8 not null,
    from_collection_mapping_id int8 not null
);
create table object_mapping
(
    id bigserial not null,
    primary key (id)
);
create table object_mapping_object_collection_mapping_per_key
(
    object_mapping_id            int8         not null,
    object_collection_mapping_id int8         not null,
    key                          varchar(255) not null,
    primary key (object_mapping_id, key)
);
create table object_mapping_object_mapping_per_key
(
    parent_id int8         not null,
    child_id  int8         not null,
    key       varchar(255) not null,
    primary key (parent_id, key)
);
create table object_mapping_value_collection_mapping_per_key
(
    object_mapping_id           int8         not null,
    value_collection_mapping_id int8         not null,
    key                         varchar(255) not null,
    primary key (object_mapping_id, key)
);
create table object_mapping_value_mapping_per_key
(
    object_mapping_id int8         not null,
    value_mapping_id  int8         not null,
    key               varchar(255) not null,
    primary key (object_mapping_id, key)
);
create table objects_from_collection_mapping
(
    id                 bigserial not null,
    element_mapping_id int8      not null,
    primary key (id)
);
create table value_collection_mapping
(
    id bigserial not null,
    primary key (id)
);
create table value_collection_mapping_element_mappings
(
    collection_mapping_id int8 not null,
    element_mapping_id    int8 not null
);
create table value_collection_mapping_from_collection_mappings
(
    collection_mapping_id      int8 not null,
    from_collection_mapping_id int8 not null
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
    id                 bigserial not null,
    element_mapping_id int8      not null,
    primary key (id)
);
alter table configuration
    add constraint UniqueIntegrationIdAndVersion unique (integration_id, version);
alter table object_collection_mapping_element_mappings
    add constraint UK_9ue5v3pqut9462hmi156v8272 unique (element_mapping_id);
alter table object_collection_mapping_from_collection_mappings
    add constraint UK_ahnvvudqy7ihpljxo0ixbv589 unique (from_collection_mapping_id);
alter table object_mapping_object_collection_mapping_per_key
    add constraint UK_2adm7ftc95kgcrvt1nfsgjw3c unique (object_collection_mapping_id);
alter table object_mapping_object_mapping_per_key
    add constraint UK_oqrcyqyttp3a5jti0qsvcowic unique (child_id);
alter table object_mapping_value_collection_mapping_per_key
    add constraint UK_156j33a8q9p9nm8el5fwq6jxi unique (value_collection_mapping_id);
alter table object_mapping_value_mapping_per_key
    add constraint UK_ptwngyxt6t1up273t6ertw40t unique (value_mapping_id);
alter table value_collection_mapping_element_mappings
    add constraint UK_lc9gapk5rkftb7dq3xnjkr7g8 unique (element_mapping_id);
alter table value_collection_mapping_from_collection_mappings
    add constraint UK_5txh0cu49n06idrqh6xvl762o unique (from_collection_mapping_id);
alter table configuration
    add constraint FKwmivbmg8cyv1hyhr9951xu47 foreign key (mapping_id) references object_mapping;
alter table instance_collection_reference
    add constraint FK6q2d8n31nhwxoe2pfbl6kru7v foreign key (instance_collection_references_ordered_id) references values_from_collection_mapping;
alter table object_collection_mapping_element_mappings
    add constraint FKe83vo803kbli19f17ly00y5m4 foreign key (element_mapping_id) references object_mapping;
alter table object_collection_mapping_element_mappings
    add constraint FKfxulc26gr7jnfhqk3bj4w2os2 foreign key (collection_mapping_id) references object_collection_mapping;
alter table object_collection_mapping_from_collection_mappings
    add constraint FK3kukap8n54aoqk5xjvmwt5f06 foreign key (from_collection_mapping_id) references objects_from_collection_mapping;
alter table object_collection_mapping_from_collection_mappings
    add constraint FK5iot1jwbkpqqgto76g20517t1 foreign key (collection_mapping_id) references object_collection_mapping;
alter table object_mapping_object_collection_mapping_per_key
    add constraint FKoajrfdkq22yk7y64xbior6oo0 foreign key (object_collection_mapping_id) references object_collection_mapping;
alter table object_mapping_object_collection_mapping_per_key
    add constraint FK8q49btex188odatuyrt2wmoem foreign key (object_mapping_id) references object_mapping;
alter table object_mapping_object_mapping_per_key
    add constraint FKqpltue1j2m8s5eclob9ddhi17 foreign key (child_id) references object_mapping;
alter table object_mapping_object_mapping_per_key
    add constraint FKnw7jt09f3aoq9mu0cu5jdhd36 foreign key (parent_id) references object_mapping;
alter table object_mapping_value_collection_mapping_per_key
    add constraint FKj21v9e9ryoue138y2630tlew0 foreign key (value_collection_mapping_id) references value_collection_mapping;
alter table object_mapping_value_collection_mapping_per_key
    add constraint FKgjg189qebrfujh4x927bcwdwl foreign key (object_mapping_id) references object_mapping;
alter table object_mapping_value_mapping_per_key
    add constraint FKmbs8y6aekhqoo3i5ugohgdmqi foreign key (value_mapping_id) references value_mapping;
alter table object_mapping_value_mapping_per_key
    add constraint FK7u1q81aimulsgn0k13ri5r9nr foreign key (object_mapping_id) references object_mapping;
alter table objects_from_collection_mapping
    add constraint FKnjogdhnvaxdb548kvep8n6ok4 foreign key (element_mapping_id) references object_mapping;
alter table value_collection_mapping_element_mappings
    add constraint FKh29twjyr20bu0681robqsemkx foreign key (element_mapping_id) references value_mapping;
alter table value_collection_mapping_element_mappings
    add constraint FK7w914r2v5bxhdik83ow4imjj4 foreign key (collection_mapping_id) references value_collection_mapping;
alter table value_collection_mapping_from_collection_mappings
    add constraint FKfm4sv2g9biu1ohhpd3463klc foreign key (from_collection_mapping_id) references values_from_collection_mapping;
alter table value_collection_mapping_from_collection_mappings
    add constraint FK5kir798qf3cs7ve1ho8c9c3if foreign key (collection_mapping_id) references value_collection_mapping;
alter table values_from_collection_mapping
    add constraint FKlssrlxhw8bn9ndq9gbe4voy1w foreign key (element_mapping_id) references value_mapping;
