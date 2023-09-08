CREATE TABLE IF NOT EXISTS arquivo
(
	id              TEXT   NOT NULL,
	status          TEXT   NOT NULL,
	valor           TEXT   NOT NULL,
	documento       TEXT   NOT NULL,
	dtProcessamento BIGINT NOT NULL,

	CONSTRAINT pk_arquivo PRIMARY KEY (id),
	CONSTRAINT ck_arquivo_status CHECK (status IN ('PENDENTE', 'SUCESSO', 'ERRO'))
);
