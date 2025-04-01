-- Inserindo alunos
INSERT INTO aluno (nome, email, telefone, ativo, data_nascimento) VALUES
('Carlos Silva', 'carlos@email.com', '11999999999', true, '1995-05-10'),
('Ana Souza', 'ana@email.com', '11888888888', true, '1998-08-22'),
('Pedro Rocha', 'pedro@email.com', '11777777777', false, '2000-11-15');

-- Inserindo pagamentos
INSERT INTO pagamento (id_aluno, valor, data_pagamento, pago) VALUES
(1, 150.00, '2024-04-01', true),
(2, 150.00, '2024-04-05', false),
(3, 150.00, '2024-03-10', true);

-- Inserindo check-ins
INSERT INTO checkin (id_aluno, data_hora) VALUES
(1, '2024-04-01 08:00:00'),
(2, '2024-04-02 09:30:00'),
(1, '2024-04-03 07:45:00');