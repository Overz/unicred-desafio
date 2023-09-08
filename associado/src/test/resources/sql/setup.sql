-- CREATE TABLE IF NOT EXISTS test.associado
-- (
-- 	uuid        TEXT        NOT NULL,
-- 	documento   VARCHAR(14) NOT NULL,
-- 	tipo_pessoa VARCHAR(2)  NOT NULL,
-- 	nome        VARCHAR(50) NOT NULL,
--
-- 	CONSTRAINT pk_associado PRIMARY KEY (uuid),
-- 	CONSTRAINT ck_associado_tipo_pessoa CHECK (tipo_pessoa IN ('PJ', 'PF'))
-- );

INSERT INTO associado (uuid, documento, tipo_pessoa, nome)
VALUES ('1', '17904033089', 'PF', 'jose'),
			 ('2', '58124967067', 'PF', 'ana'),
			 ('3', '50539925098', 'PF', 'mario'),
			 ('4', '38497387000106', 'PJ', '0800 Alimentos LTDA');
