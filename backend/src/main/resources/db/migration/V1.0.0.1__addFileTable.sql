CREATE TABLE IF NOT EXISTS file
(
    id        uuid          NOT NULL,
    content   bytea         NULL,
    filename  varchar(4096) NOT NULL,
    mime_type varchar(512)  NULL,
    CONSTRAINT file_pkey PRIMARY KEY (id)
);