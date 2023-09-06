# unicred-desafio

As especificações do desafio estão neste [arquivo](./UNICRED_Prova_Tecnica_Java.pdf).

## Setup

- Java: 17
- Pacote Common: executar `mvn clean install` para que os demais projetos possam acessar os arquivos
	utilitários
- Infra: O projeto utiliza de banco de dados `PostgreSQL` & `RabbitMQ`, o
	arquivo [docker-compose.yaml](./infra/docker-compose.yaml) possui toda a configuração necessária
