-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile


create table archetype_info
(
    concept varchar(512),
    archetype_id varchar(512) primary key,
    description varchar(512),
    status varchar(512),
    created_at varchar(512),
    updated_at varchar(512),
    asset_cid varchar(512),
    ckm_path varchar(512),
    adl_path varchar(512),
    xml_path varchar(512)
);

create table template_info
(
    template  varchar(512),
    template_id varchar(512) primary key,
    status varchar(512),
    created_at varchar(512),
    updated_at varchar(512),
    asset_cid varchar(512),
    ckm_path varchar(512),
    adl_path varchar(512)
)