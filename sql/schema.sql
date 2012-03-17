
    create table CandidateJPA (
        id varchar(255) not null,
        firstName varchar(255),
        lastName varchar(255),
        primary key (id)
    );

    create table ElecteurJPA (
        id varchar(255) not null,
        primary key (id)
    );

    create table PropositionJPA (
        id varchar(255) not null,
        text varchar(255),
        primary key (id)
    );

    create table TagJPA (
        id varchar(255) not null,
        name varchar(255),
        primary key (id)
    );
