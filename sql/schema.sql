
    create table CandidacyJPA (
        id varchar(255) not null,
        nbCandidates integer not null,
        candidate1_id varchar(255),
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

    create table GlobalStatsJPA (
        DTYPE varchar(31) not null,
        id bigint generated by default as identity (start with 1),
        voter_id bigint,
        primary key (id)
    );

    create table GlobalStatsJPA_StatsCandidacyJPA (
        GlobalStatsJPA_id bigint not null,
        statsCandidacy_id bigint not null,
        unique (statsCandidacy_id)
    );

    create table GlobalStatsJPA_StatsThemeJPA (
        GlobalStatsJPA_id bigint not null,
        statsTheme_id bigint not null,
        unique (statsTheme_id)
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
        question_id bigint,
        voter_id bigint,
        primary key (id)
    );

    create table StatsCandidacyJPA (
        id bigint generated by default as identity (start with 1),
        answered integer not null,
        rights integer not null,
        candidacy_id varchar(255),
        primary key (id)
    );

    create table StatsThemeJPA (
        id bigint generated by default as identity (start with 1),
        answered integer not null,
        rights integer not null,
        tag_id varchar(255),
        primary key (id)
    );

    create table TagJPA (
        id varchar(255) not null,
        level integer not null,
        name varchar(255),
        namespace varchar(255),
        primary key (id)
    );

    create table VoterJPA (
        id bigint generated by default as identity (start with 1),
        username varchar(255),
        primary key (id)
    );

    alter table CandidacyJPA 
        add constraint FK40EBD353CD710A51 
        foreign key (candidate1_id) 
        references CandidateJPA;

    alter table CandidacyJPA_CandidateJPA 
        add constraint FKA17D20E46ADE0FC7 
        foreign key (CandidacyJPA_id) 
        references CandidacyJPA;

    alter table CandidacyJPA_CandidateJPA 
        add constraint FKA17D20E4CD8F0ACF 
        foreign key (candidates_id) 
        references CandidateJPA;

    alter table GlobalStatsJPA 
        add constraint FK882039FFAB29F72 
        foreign key (voter_id) 
        references VoterJPA;

    alter table GlobalStatsJPA_StatsCandidacyJPA 
        add constraint FK81B2C4B294D961D6 
        foreign key (statsCandidacy_id) 
        references StatsCandidacyJPA;

    alter table GlobalStatsJPA_StatsCandidacyJPA 
        add constraint FK81B2C4B25AA86527 
        foreign key (GlobalStatsJPA_id) 
        references GlobalStatsJPA;

    alter table GlobalStatsJPA_StatsThemeJPA 
        add constraint FKFD6FC651FF269694 
        foreign key (statsTheme_id) 
        references StatsThemeJPA;

    alter table GlobalStatsJPA_StatsThemeJPA 
        add constraint FKFD6FC6515AA86527 
        foreign key (GlobalStatsJPA_id) 
        references GlobalStatsJPA;

    alter table PropositionJPA 
        add constraint FK197D2545F86CEDC1 
        foreign key (tagLevel1_id) 
        references TagJPA;

    alter table PropositionJPA 
        add constraint FK197D25453ADE8F72 
        foreign key (candidacy_id) 
        references CandidacyJPA;

    alter table PropositionJPA_TagJPA 
        add constraint FK715E3E3BCE3BE9AF 
        foreign key (tags_id) 
        references TagJPA;

    alter table PropositionJPA_TagJPA 
        add constraint FK715E3E3BE77DE347 
        foreign key (PropositionJPA_id) 
        references PropositionJPA;

    alter table QuestionJPA 
        add constraint FK9CE339F5F86CEDC1 
        foreign key (tagLevel1_id) 
        references TagJPA;

    alter table QuestionJPA 
        add constraint FK9CE339F53ADE8F72 
        foreign key (candidacy_id) 
        references CandidacyJPA;

    alter table ResponseJPA 
        add constraint FKD188BFBAFAB29F72 
        foreign key (voter_id) 
        references VoterJPA;

    alter table ResponseJPA 
        add constraint FKD188BFBAB35BA17C 
        foreign key (question_id) 
        references QuestionJPA;

    alter table StatsCandidacyJPA 
        add constraint FK2795E1523ADE8F72 
        foreign key (candidacy_id) 
        references CandidacyJPA;

    alter table StatsThemeJPA 
        add constraint FKCD9A32F1F5FB228E 
        foreign key (tag_id) 
        references TagJPA;
