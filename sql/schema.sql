
    create table CandidacyJPA (
        id varchar(255) not null,
        primary key (id)
    );

    create table CandidacyJPA_CandidateJPA (
        CandidacyJPA_id varchar(255) not null,
        candidates_id varchar(255) not null,
        primary key (CandidacyJPA_id, candidates_id),
        unique (candidates_id)
    );

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
        text VARCHAR(1000000),
        candidacy_id varchar(255) not null,
        tagLevel1_id varchar(255),
        primary key (id)
    );

    create table PropositionJPA_TagJPA (
        PropositionJPA_id varchar(255) not null,
        tags_id varchar(255) not null,
        primary key (PropositionJPA_id, tags_id)
    );

    create table QuestionJPA (
        id bigint generated by default as identity (start with 1),
        rightAnswer boolean not null,
        text VARCHAR(1000000),
        candidacy_id varchar(255),
        tagLevel1_id varchar(255),
        primary key (id)
    );

    create table ResponseJPA (
        id bigint generated by default as identity (start with 1),
        correct boolean not null,
        occurence integer not null,
        username varchar(255),
        electeur_id varchar(255),
        question_id bigint,
        primary key (id)
    );

    create table TagJPA (
        id varchar(255) not null,
        level integer not null,
        name varchar(255),
        namespace varchar(255),
        primary key (id)
    );

    alter table CandidacyJPA_CandidateJPA 
        add constraint FKA17D20E4260C8C82 
        foreign key (CandidacyJPA_id) 
        references CandidacyJPA;

    alter table CandidacyJPA_CandidateJPA 
        add constraint FKA17D20E488BD878A 
        foreign key (candidates_id) 
        references CandidateJPA;

    alter table PropositionJPA 
        add constraint FK197D2545E47A52BC 
        foreign key (tagLevel1_id) 
        references TagJPA;

    alter table PropositionJPA 
        add constraint FK197D2545F60D0C2D 
        foreign key (candidacy_id) 
        references CandidacyJPA;

    alter table PropositionJPA_TagJPA 
        add constraint FK715E3E3BBA494EAA 
        foreign key (tags_id) 
        references TagJPA;

    alter table PropositionJPA_TagJPA 
        add constraint FK715E3E3B91001D42 
        foreign key (PropositionJPA_id) 
        references PropositionJPA;

    alter table QuestionJPA 
        add constraint FK9CE339F5E47A52BC 
        foreign key (tagLevel1_id) 
        references TagJPA;

    alter table QuestionJPA 
        add constraint FK9CE339F5F60D0C2D 
        foreign key (candidacy_id) 
        references CandidacyJPA;

    alter table ResponseJPA 
        add constraint FKD188BFBA6CA2C747 
        foreign key (electeur_id) 
        references ElecteurJPA;

    alter table ResponseJPA 
        add constraint FKD188BFBA985D2161 
        foreign key (question_id) 
        references QuestionJPA;