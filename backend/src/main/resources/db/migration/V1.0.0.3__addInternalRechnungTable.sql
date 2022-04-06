CREATE TABLE IF NOT EXISTS internal_rechnung
(
    id                      uuid          NOT NULL,
    dossier_number          varchar(512)  NOT NULL,
    dossier_number_validity varchar(4096) NOT NULL,
    kesb_standort_id        varchar(4096) NULL,
    sozialdienst_id         varchar(4096) NULL,
    iban                    varchar(4096) NULL,
    reference_number        varchar(4096) NULL,
    status                  varchar(4096) NULL,
    rechnung_file_id        uuid          NOT NULL,

    CONSTRAINT internal_rechnung_pkey PRIMARY KEY (id)
);