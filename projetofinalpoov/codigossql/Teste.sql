CREATE TABLE pessoa
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    dataNascimento date,
    situacao text,
    PRIMARY KEY (codigo)
);

CREATE TABLE vacina
(
    codigo serial NOT NULL,
    nome text,
    descricao text,
    situacao text,
    PRIMARY KEY (codigo)
);

CREATE TABLE aplicacao
(
    codigo serial NOT NULL,
    data date,
    codigo_pessoa int,
    codigo_vacina int,
    situacao text,
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo),
    FOREIGN KEY (codigo_vacina) REFERENCES vacina(codigo)
);

--Inserindo dados genéricos na tabela de vacina
INSERT INTO vacina (nome, descricao, situacao) VALUES
('Vacina Genérica 1', 'Descrição da Vacina Genérica 1', 'ATIVO'),
('Vacina Genérica 2', 'Descrição da Vacina Genérica 2', 'ATIVO'),
('Vacina Genérica 3', 'Descrição da Vacina Genérica 3', 'ATIVO'),
('Vacina Genérica 4', 'Descrição da Vacina Genérica 4', 'ATIVO'),
('Vacina Genérica 5', 'Descrição da Vacina Genérica 5', 'ATIVO'),
('Vacina Genérica 6', 'Descrição da Vacina Genérica 6', 'ATIVO'),
('Vacina Genérica 7', 'Descrição da Vacina Genérica 7', 'ATIVO'),
('Vacina Genérica 8', 'Descrição da Vacina Genérica 8', 'ATIVO'),
('Vacina Genérica 9', 'Descrição da Vacina Genérica 9', 'ATIVO'),
('Vacina Genérica 10', 'Descrição da Vacina Genérica 10', 'ATIVO');

-- Inserindo dados genéricos na tabela de pessoa
INSERT INTO pessoa (nome, cpf, dataNascimento, situacao) VALUES
('Pessoa Genérica 1', '12345678901', '2000-01-01', 'ATIVO'),
('Pessoa Genérica 2', '12345678902', '2000-01-02', 'ATIVO'),
('Pessoa Genérica 3', '12345678903', '2000-01-03', 'ATIVO'),
('Pessoa Genérica 4', '12345678904', '2000-01-04', 'ATIVO'),
('Pessoa Genérica 5', '12345678905', '2000-01-05', 'ATIVO'),
('Pessoa Genérica 6', '12345678906', '2000-01-06', 'ATIVO'),
('Pessoa Genérica 7', '12345678907', '2000-01-07', 'ATIVO'),
('Pessoa Genérica 8', '12345678908', '2000-01-08', 'ATIVO'),
('Pessoa Genérica 9', '12345678909', '2000-01-09', 'ATIVO'),
('Pessoa Genérica 10', '12345678910', '2000-01-10', 'ATIVO');