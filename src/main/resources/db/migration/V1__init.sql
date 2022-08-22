create sequence hibernate_sequence start 1 increment 1;
create table applicant_configuration_field_properties
(
    applicant_configuration_field_id int8 not null,
    key                              varchar(255),
    property_order                   int4,
    source                           int4
);
create table application_configuration
(
    id                       int8 not null,
    national_identity_number varchar(255),
    organisation_number      varchar(255),
    is_protected             bool,
    primary key (id)
);
create table application_configuration_field
(
    id                         int8 not null,
    field                      varchar(255),
    value_build_strategy       int4,
    value                      varchar(255),
    applicant_configuration_id int8,
    primary key (id)
);
create table case_configuration
(
    id                     int8 not null,
    case_creation_strategy int4,
    case_number            varchar(255),
    primary key (id)
);
create table case_configuration_field
(
    id                    int8 not null,
    field                 varchar(255),
    value_build_strategy  int4,
    value                 varchar(255),
    case_configuration_id int8,
    primary key (id)
);
create table case_configuration_field_properties
(
    case_configuration_field_id int8 not null,
    key                         varchar(255),
    property_order              int4,
    source                      int4
);
create table document_configuration
(
    id int8 not null,
    primary key (id)
);
create table document_configuration_field
(
    id                        int8 not null,
    field                     varchar(255),
    value_build_strategy      int4,
    value                     varchar(255),
    document_configuration_id int8,
    primary key (id)
);
create table document_configuration_field_properties
(
    document_configuration_field_id int8 not null,
    key                             varchar(255),
    property_order                  int4,
    source                          int4
);
create table integration_configuration
(
    id                                int8    not null,
    created_date                      timestamp,
    description                       varchar(255),
    destination                       varchar(255),
    is_published                      boolean not null,
    org_id                            varchar(255),
    source_application_id             varchar(255),
    source_application_integration_id varchar(255),
    version                           int4    not null,
    applicant_configuration_id        int8,
    case_configuration_id             int8,
    document_configuration_id         int8,
    record_configuration_id           int8,
    primary key (id)
);
create table record_configuration
(
    id int8 not null,
    primary key (id)
);
create table record_configuration_field
(
    id                      int8 not null,
    field                   varchar(255),
    value_build_strategy    int4,
    value                   varchar(255),
    record_configuration_id int8,
    primary key (id)
);
create table record_configuration_field_properties
(
    record_configuration_field_id int8 not null,
    key                           varchar(255),
    property_order                int4,
    source                        int4
);
alter table applicant_configuration_field_properties
    add constraint FK3dy9aqraw58bt3rh5sngwjk5r foreign key (applicant_configuration_field_id) references application_configuration_field;
alter table application_configuration_field
    add constraint FKoycquv8e583offbsaxdcuge4j foreign key (applicant_configuration_id) references application_configuration;
alter table case_configuration_field
    add constraint FKn56mpejyeo2mx0finbtg1rmwe foreign key (case_configuration_id) references case_configuration;
alter table case_configuration_field_properties
    add constraint FK72rejbaa7l886j1jfh63j9tg8 foreign key (case_configuration_field_id) references case_configuration_field;
alter table document_configuration_field
    add constraint FKlykedmisgjaxxa7sj3kn27mui foreign key (document_configuration_id) references document_configuration;
alter table document_configuration_field_properties
    add constraint FK3o18ks76urw9vu77509pt9d9h foreign key (document_configuration_field_id) references document_configuration_field;
alter table integration_configuration
    add constraint FK11n3fr6457je51kl9nxe968wt foreign key (applicant_configuration_id) references application_configuration;
alter table integration_configuration
    add constraint FKbyuyuuob8v1d5uhgpv5be0dwx foreign key (case_configuration_id) references case_configuration;
alter table integration_configuration
    add constraint FKhb54g4urjpx7ucx3xxjuauev1 foreign key (document_configuration_id) references document_configuration;
alter table integration_configuration
    add constraint FKr9er5y6ah57v75cvod4dqqhhp foreign key (record_configuration_id) references record_configuration;
alter table record_configuration_field
    add constraint FKnl549tkropjce64q1ew3aphhv foreign key (record_configuration_id) references record_configuration;
alter table record_configuration_field_properties
    add constraint FK11yrlgdgqhonfssj0wwas4x8j foreign key (record_configuration_field_id) references record_configuration_field;
