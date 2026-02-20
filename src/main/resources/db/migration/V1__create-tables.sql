-- =========================================
-- TABELA ANALISTA
-- =========================================
CREATE TABLE analista (
    id_analista INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL UNIQUE
);


-- =========================================
-- TABELA EMPRESA
-- =========================================
CREATE TABLE empresa (
    id_empresa INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    status_contrato ENUM('ATIVO','CANCELADO') DEFAULT 'ATIVO',
    operadora VARCHAR(150) NOT NULL,
    dt_aniversario DATE NOT NULL,
    porte ENUM('PME','PJ') NOT NULL,
    mensalidade_atual DECIMAL(15,2) NOT NULL,
    status_renovacao ENUM('EM_NEGOCIACAO','REAJUSTADO','PENDENTE','EM_ATRASO'),
    id_analista INT NOT NULL,
    FOREIGN KEY (id_analista) REFERENCES analista(id_analista)
);


-- =========================================
-- TABELA REAJUSTE
-- =========================================
CREATE TABLE reajuste (
    id_reajuste INT PRIMARY KEY AUTO_INCREMENT,
    id_empresa INT NOT NULL,
    ano_referencia INT NOT NULL,
    status ENUM('AGUARDANDO_OPERADORA','EM_NEGOCIACAO','FECHADO','RECUSADO','CANCELADO'),

    -- Valor pago antes do reajuste (base congelada)
    vl_ultima_fatura DECIMAL(15,2) NOT NULL,

    -- Percentual inicial enviado pela operadora
    porcentagem_oferecida_operadora DECIMAL(5,2),

    -- Valor da fatura com percentual inicial aplicado
    vl_com_primeira_porcentagem DECIMAL(15,2),

    dt_envio DATE,

    UNIQUE (id_empresa, ano_referencia),
    FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
);


-- =========================================
-- TABELA NEGOCIACAO
-- =========================================
CREATE TABLE negociacao (
    id_negociacao INT PRIMARY KEY AUTO_INCREMENT,
    id_reajuste INT NOT NULL,
    status ENUM('EM_ANDAMENTO','FINALIZADA','CANCELADA') DEFAULT 'EM_ANDAMENTO',
    dt_inicio DATE NOT NULL,
    dt_fim DATE,
    motivo_encerramento VARCHAR(255),

    FOREIGN KEY (id_reajuste) REFERENCES reajuste(id_reajuste)
);


-- =========================================
-- TABELA INTERACAO
-- =========================================
CREATE TABLE interacao (
    id_interacao INT PRIMARY KEY AUTO_INCREMENT,
    id_negociacao INT NOT NULL,

    tipo ENUM('OPERADORA','CORRETORA') NOT NULL,

    porcentagem_proposta DECIMAL(5,2) NOT NULL,

    -- Valor da fatura resultante dessa proposta
    vl_mensal_resultante DECIMAL(15,2),

    observacao VARCHAR(255),
    dt_interacao DATE NOT NULL,

    -- Marca proposta oficialmente aceita
    is_aceita BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (id_negociacao) REFERENCES negociacao(id_negociacao)
);


-- =========================================
-- TABELA DOCUMENTOS
-- =========================================
CREATE TABLE documentos (
    id_documento INT PRIMARY KEY AUTO_INCREMENT,
    nome_documento VARCHAR(200) NOT NULL,
    link_documento VARCHAR(300) NOT NULL,
    id_reajuste INT,

    UNIQUE (link_documento),
    FOREIGN KEY (id_reajuste) REFERENCES reajuste(id_reajuste)
);