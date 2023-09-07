# RabbitMQ

---

## Como funciona o PUB/SUB do RabbitMQ?

O PUB/SUB (publicar/assinar) é um padrão de mensageria usado no RabbitMQ para comunicação entre aplicativos. No RabbitMQ, isso é geralmente implementado usando "exchanges" e "queues". Aqui está uma visão geral de como funciona:

- **Publisher (Editor)**: É um aplicativo que envia mensagens para o RabbitMQ usando um canal (channel).
- **Exchange**: Um exchange é um componente central no RabbitMQ que recebe mensagens dos publishers e as encaminha para as filas. Existem vários tipos de exchanges, incluindo direct, topic, fanout, e headers, que determinam como as mensagens são roteadas para as filas.
- **Queue (Fila)**: É um local onde as mensagens são armazenadas até que sejam consumidas por um ou mais consumidores. Cada fila tem um nome.
- **Subscriber (Assinante)**: É um aplicativo que cria uma conexão com o RabbitMQ, cria uma fila e se inscreve nela para receber mensagens. Quando uma mensagem é publicada em uma fila, o RabbitMQ a encaminha para todas as filas inscritas nesse exchange.

---

## Por que nomear uma Queue?

Nomear uma fila no RabbitMQ é importante por várias razões:

- **Identificação**: Um nome descritivo torna mais fácil identificar a fila em um sistema com várias filas.
- **Configuração**: O nome da fila é usado na configuração do RabbitMQ para vincular trocas a filas e configurar políticas de fila.
- **Depuração**: Quando ocorrem problemas ou erros, é mais fácil rastrear o problema se você puder identificar qual fila está envolvida.

---

## O que são Topic Exchanges ou Exchange no RabbitMQ?

No RabbitMQ, um exchange é um componente central que recebe mensagens dos publishers e as encaminha para as filas. O tipo de exchange determina como as mensagens são roteadas para as filas. Os tipos de exchanges mais comuns são:

- **Direct Exchange**: Roteia mensagens para filas com base em uma chave de roteamento (routing key) exata. A mensagem é entregue a todas as filas cuja chave de roteamento corresponda exatamente à chave definida na mensagem.
- **Fanout Exchange**: Roteia mensagens para todas as filas associadas a ele. Ignora completamente as chaves de roteamento.
- **Topic Exchange**: Roteia mensagens com base em padrões de chave de roteamento. As filas são vinculadas a um tópico com uma chave de roteamento que pode conter wildcards (* e #) para corresponder a várias chaves. Isso permite roteamento flexível com base em padrões.

---

## O que são as "routing key" do RabbitMQ e quando usar?

A "routing key" (chave de roteamento) é uma propriedade que é definida nas mensagens enviadas para um exchange. A chave de roteamento é usada pelo exchange para determinar a qual fila a mensagem deve ser encaminhada. O uso da chave de roteamento é especialmente relevante em exchanges do tipo "direct" e "topic". Aqui estão algumas informações sobre quando usar a chave de roteamento:

- **Direct Exchange**: Use a chave de roteamento para especificar exatamente qual fila deve receber a mensagem. A mensagem será entregue apenas às filas com uma chave de roteamento que corresponda exatamente à chave definida na mensagem.
- **Topic Exchange**: Use a chave de roteamento com padrões de roteamento flexíveis. Isso permite que você defina padrões de chave de roteamento (por exemplo, "logs.*" ou "user.#") para encaminhar mensagens para as filas apropriadas com base em critérios específicos.

Em resumo, a chave de roteamento é uma ferramenta poderosa para controlar como as mensagens são encaminhadas no RabbitMQ, permitindo roteamento específico ou baseado em padrões, dependendo do tipo de exchange que você está usando.
