# unicred-desafio

As especificações do desafio estão neste [arquivo](./UNICRED_Prova_Tecnica_Java.pdf).

- [Setup](#setup)
- [Executando](#executando)
- [RabbitMQ](#rabbitmq)

---

## Setup

- Java: 17
- Pacote Common: executar `mvn clean install` para que os demais projetos possam acessar os arquivos
	utilitários
- Infra: O projeto utiliza de banco de dados `PostgreSQL` & `RabbitMQ`, o
	arquivo [docker-compose.yaml](./infra/docker-compose.yaml) possui toda a configuração necessária

---

## Executando

Cada projeto possui um arquivo `.env` com variáveis de ambiente necessárias para executar o projeto, se
necessário podem ser alteradas ou se **necessário** inserir manualmente as variáveis de ambiente em tempo de
execução.

Cada aplicação possui **`UM`** banco de dados chamados `associado`, `boleto`, `processamento` que são criados
automáticamente ao iniciar a infra com docker.

---

## RabbitMQ

Neste [documento](./RabbitMQ.md) explica de como funciona o Pub/Sub do RabbitMQ.
