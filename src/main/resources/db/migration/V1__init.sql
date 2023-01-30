create table configuration
(
    id                      bigserial not null,
    comment                 varchar(255),
    completed               boolean   not null,
    integration_id          int8      not null,
    integration_metadata_id int8      not null,
    version                 int4,
    configuration_id        int8      not null,
    primary key (id)
);
create table element_collection_mapping
(
    id bigserial not null,
    primary key (id)
);
create table element_collection_mapping_element_mappings
(
    parent_element_collection_mapping_id int8 not null,
    child_element_mapping_id             int8 not null
);
create table element_collection_mapping_elements_from_collection_mappings
(
    parent_element_collection_mapping_id      int8 not null,
    child_elements_from_collection_mapping_id int8 not null
);
create table element_mapping
(
    id bigserial not null,
    primary key (id)
);
create table element_mapping_element_collection_mapping_per_key
(
    parent_element_mapping_id           int8         not null,
    child_element_collection_mapping_id int8         not null,
    key                                 varchar(255) not null,
    primary key (parent_element_mapping_id, key)
);
create table element_mapping_element_mapping_per_key
(
    parent_element_mapping_id int8         not null,
    child_element_mapping_id  int8         not null,
    key                       varchar(255) not null,
    primary key (parent_element_mapping_id, key)
);
create table element_mapping_value_mapping_per_key
(
    parent_element_mapping_id int8         not null,
    child_value_mapping_id    int8         not null,
    key                       varchar(255) not null,
    primary key (parent_element_mapping_id, key)
);
create table elements_from_collection_mapping
(
    id                 bigserial not null,
    element_mapping_id int8      not null,
    primary key (id)
);
create table instance_collection_references_ordered
(
    elements_from_collection_mapping_id    int8 not null,
    instance_collection_references_ordered varchar(255),
    index                                  int4 not null,
    primary key (elements_from_collection_mapping_id, index)
);
create table value_mapping
(
    id             bigserial    not null,
    mapping_string varchar(255) not null,
    type           varchar(255) not null,
    primary key (id)
);
alter table configuration
    add constraint UniqueIntegrationIdAndVersion unique (integration_id, version);
alter table element_collection_mapping_element_mappings
    add constraint UK_mfc3wdo5olwokkv76f07ray84 unique (child_element_mapping_id);
alter table element_collection_mapping_elements_from_collection_mappings
    add constraint UK_lmpa06m5nivor5jge8lp64o6h unique (child_elements_from_collection_mapping_id);
alter table element_mapping_element_collection_mapping_per_key
    add constraint UK_185oyf216mtugal285monrvf6 unique (child_element_collection_mapping_id);
alter table element_mapping_element_mapping_per_key
    add constraint UK_cguagp5jk5u3yh6o8nyl7adr unique (child_element_mapping_id);
alter table element_mapping_value_mapping_per_key
    add constraint UK_juqik3o5f0sm4surga4hblsqo unique (child_value_mapping_id);
alter table configuration
    add constraint FKrugtd5wxkb7g3x4kmbu1y6hqa foreign key (configuration_id) references element_mapping;
alter table element_collection_mapping_element_mappings
    add constraint FKiwqmg25wlvh7bytgssimhliu3 foreign key (child_element_mapping_id) references element_mapping;
alter table element_collection_mapping_element_mappings
    add constraint FK335xbj32sbf9f86pgyausq1u8 foreign key (parent_element_collection_mapping_id) references element_collection_mapping;
alter table element_collection_mapping_elements_from_collection_mappings
    add constraint FK438tfbq61niin6mfq6cgv627k foreign key (child_elements_from_collection_mapping_id) references elements_from_collection_mapping;
alter table element_collection_mapping_elements_from_collection_mappings
    add constraint FK3nsyth227xkesg3hfs0xpdcr9 foreign key (parent_element_collection_mapping_id) references element_collection_mapping;
alter table element_mapping_element_collection_mapping_per_key
    add constraint FKmj8cu5arseyp5x2ju3wik4dqk foreign key (child_element_collection_mapping_id) references element_collection_mapping;
alter table element_mapping_element_collection_mapping_per_key
    add constraint FKr7r2q62rae5cc3oeelwoiixdt foreign key (parent_element_mapping_id) references element_mapping;
alter table element_mapping_element_mapping_per_key
    add constraint FK87n8g5oj0460gwvib0ju4h8rw foreign key (child_element_mapping_id) references element_mapping;
alter table element_mapping_element_mapping_per_key
    add constraint FKrsfh014frcgs4inu13qhlpp2u foreign key (parent_element_mapping_id) references element_mapping;
alter table element_mapping_value_mapping_per_key
    add constraint FKsme0tt0q250e120rn1v4icw5p foreign key (child_value_mapping_id) references value_mapping;
alter table element_mapping_value_mapping_per_key
    add constraint FKds6s4icj2ncnttlsuk75nr4qr foreign key (parent_element_mapping_id) references element_mapping;
alter table elements_from_collection_mapping
    add constraint FK5us92lxojr44pigvwcljd8bh3 foreign key (element_mapping_id) references element_mapping;
alter table instance_collection_references_ordered
    add constraint FKr7c5h3bsqltx6wbhh4irjiih5 foreign key (elements_from_collection_mapping_id) references elements_from_collection_mapping;
