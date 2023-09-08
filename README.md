# unicred-desafio

As especificações do desafio estão neste [arquivo](./UNICRED_Prova_Tecnica_Java.pdf).

- [Setup](#setup)
- [Executando](#executando)
- [RabbitMQ](#rabbitmq)

> **ESTA APLICAÇÃO NÃO POSSUI `CONTEXT-PATH`**

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

Todos os eventos emitidos estão no pacote `common`.

Em resumo:

- **ASSOCIADO:** Ao tentar excluir o associado, uma mensagem para a fila `direct:associado` é enviada para o
	módulo `boleto` que verifica se o associado possui boletos pendentes para o pagamento, caso sim,
	a exclusão do associado não é permitida.

- **BOLETO:** Ao criar um novo boleto, o módulo `boleto` envia uma mensagem para a fila `direct:boleto` ao
	módulo `associado` que consulta dados do associado informado na requisição para criação do boleto
	no modulo `boleto` e retorna essas informações ou `nulo`caso não encontrado.

- **ARQUIVO:** Ao processar em paralelo um diretório de arquivos que contem arquivos `.csv` (alterado no
	arquivo application.yaml do módulo), uma mensagem `direct:arquivo` é enviada para o módulo `boleto` que
	também consulta o `documento`(cpf/cnpj) do associado cadastrado no arquivo processado no módulo `associado`,
	caso todos os dados forem _OK_, o boleto será cadastrado.

---

## Mocks

O módulo `arquivo` possui um arquivo `.csv` neste [caminho](./arquivo/src/main/resources/mocks/0001-mock.csv)
podendo ser utilizado para o envio/teste do processamento de arquivos

_Observação: o associado com o mesmo CPF/CNPJ de pelo menos uma linha do arquivo de mock precisa estar
cadastrado antes no banco de dados para que tudo funcione perfeitamente._

---

## Swagger

A documentação do swagger de cada serviço está disponível através da
url `http://localhost:<port>/swagger-ui/index.html`

Cada serviço disponibiliza de forma **estática** um arquivo `swagger.yaml` através do
path `http://localhost:<port>/swagger.yaml`, utilizando a lib
`org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0` é consumido através da configuração:

```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    url: "/swagger.yaml"
    disable-swagger-default-url: true
```

---
