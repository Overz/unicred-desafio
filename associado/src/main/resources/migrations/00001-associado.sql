CREATE TABLE IF NOT EXISTS associado
(
	uuid        TEXT        NOT NULL,
	documento   VARCHAR(14) NOT NULL,
	tipo_pessoa VARCHAR(2)  NOT NULL,
	nome        VARCHAR(50) NOT NULL,

	CONSTRAINT pk_associado PRIMARY KEY (uuid),
	CONSTRAINT ck_associado_tipo_pessoa CHECK (tipo_pessoa IN ('PJ', 'PF'))
);

CREATE UNIQUE INDEX u_idx_associado_documento ON associado (documento);

COMMENT ON COLUMN associado.uuid IS 'ID';
COMMENT ON COLUMN associado.documento IS 'CPF/CNPJ';
COMMENT ON COLUMN associado.tipo_pessoa IS 'PF (PESSOA FISICA), PJ (PESSOA JURIDICA)';
COMMENT ON COLUMN associado.nome IS 'Nome do individuo';
