# Sistema Bancário com JPA e Postgresql

Este é um modelo de um sistema bancário básico que utiliza JPA para a persistência de dados e Postgresql como banco de dados. Ele foi desenvolvido para fins educacionais e pode ser usado como base para projetos mais complexos.

## Funcionalidades

- Cadastro de pessoas
- Cadastro de contas bancárias
- Realização de depósitos e saques
- Consulta de saldo e extrato bancário
- Histórico de transferências realizadas
- Histórico de transferências realizadas filtradas por mês e ano

## Tecnologias

- Java 17
- Spring Boot 3.0.6
- Spring Data JPA
- Postgresql 15

## Configuração

Antes de executar o sistema, certifique-se de que possui o Java 17 e o Postgresql 15 instalados em sua máquina. É necessário também configurar as credenciais de acesso ao banco de dados no arquivo `application.properties` para um banco em PostgreSQL que você possua localmente.

## Endpoints

Os endpoints disponíveis no sistema são:

- `/pessoa/`: `(GET)` Mostra todas as pessoas registradas no sistema
- `/pessoa/`: `(POST)` Insere a pessoa no body do Request no sistema
    - Exemplo de body válido:
    - ```{ "cpf": "000.936.534-68", "nome": "Fernando", "dataNascimento": "1930-03-13" }```
- `/pessoa/{cpf}`: `(DELETE)` Deleta a pessoa com CPF indicado do sistema (A pessoa não pode possuir contas)
- `/conta/{cpf}`: `(POST)`Cria uma conta para uma pessoa com CPF indicado
- `/conta/{id}` : `(DELETE)` Deleta conta com ID indicado do sistema. (A conta não pode possuir transações)
- `/conta/block/{id}`: `(PUT)` Bloqueia a conta do ID especificado.
- `/conta/unblock/{id}`: `(PUT)` Desbloqueia a conta do ID especificado.
- `/transf/dep/`: `(POST)` Deposita valor no body para a conta de ID também especificado no body
    - Exemplo de body para essa operação:
    - ```{ "idConta": 1, "valor":2000.00 }```
- `/transf/saq/`: `(POST)` Saca valor no body para a conta de ID também especificado no body
    - Exemplo de body para essa operação:
    - ```{ "idConta": 1, "valor":2000.00 }```
- `/tranf/saldo/{idConta}`: `(GET)` Mostra o saldo de uma determinada conta
- `/saldo/{idConta}/{ano}/{mes}`: `(GET)` Mostra o saldo de uma determinada conta em determinado ano e mês.
