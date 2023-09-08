CREATE TABLE IF NOT EXISTS boleto
(
	uuid                  TEXT           NOT NULL,
	valor                 DECIMAL(10, 2) NOT NULL,
	vencimento            TIMESTAMPTZ    NOT NULL,
	uuid_associado        TEXT           NOT NULL,
	documento_pagador     VARCHAR(14)    NOT NULL,
	nome_pagador          VARCHAR(50)    NOT NULL,
	nome_fantasia_pagador VARCHAR(50),
	situacao              TEXT           NOT NULL,

	CONSTRAINT pk_boleto PRIMARY KEY (uuid),
	CONSTRAINT ck_boleto_situacao CHECK (situacao IN ('PAGO', 'PENDENTE', 'CANCELADO')),
	CONSTRAINT ck_boleto_vencimento CHECK (vencimento >= now())
);

COMMENT ON COLUMN boleto.uuid IS 'ID';
COMMENT ON COLUMN boleto.valor IS 'Valor do pagamento do boleto';
COMMENT ON COLUMN boleto.vencimento IS 'Data de vencimento';
COMMENT ON COLUMN boleto.uuid_associado IS 'ID do associado';
COMMENT ON COLUMN boleto.documento_pagador IS 'Documento de identificação do pagador (CPF/CNPJ)';
COMMENT ON COLUMN boleto.nome_pagador IS 'Nome do individuo';
COMMENT ON COLUMN boleto.nome_fantasia_pagador IS 'Nome fantasia do pagador';
COMMENT ON COLUMN boleto.situacao IS 'Situação atual do boleto';
