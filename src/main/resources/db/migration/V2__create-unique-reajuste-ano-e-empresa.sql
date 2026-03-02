ALTER TABLE reajuste
ADD CONSTRAINT uk_empresa_ano UNIQUE (id_empresa, ano_referencia);