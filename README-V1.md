# Teste Desenvolvedor Java/Kotlin Backend

Este é o teste para Desevolvedor Java/Kotlin da Estapar.

O objetivo é criar um sistema de gestão de estacionamentos, que controla o número de vagas em aberto, 
entrada, saida e faturameto do setor.

O teste não precisa estar 100% completo, iremos avaliar até o ponto onde você conseguiu chegar.

## Requisitos técnicos
* Utilizar Java ou Kotlin
* Frameworks Spring ou Micronaut
* Utilize banco de dados MySQL ou Postgres
* Crie uma Documentação Técnica da API e do sistema.

### Simulador de entrada e saida de veiculos (Provido pela Estapar, para facilitar o teste)
Abaixo segue a instrução para rodar o simulador de entrada e saida de veiculos. Ele é iniciado dentro de um docker, 
após a inicialização faça uma chamada para o endpoint ```GET``` ```/garage``` o e você recebe a configuração da garagem, 
após alguns segundos desta chamada o simulador começa e enviar eventos de entradas e saidas de veículos da garagem.

Considere que vamos modificar os parametros no nosso teste para avaliar como a sua aplicação se comporta. 

O Simulador pode ser inicializado com o comando:
```bash

 docker run -d --network="host" cfontes0estapar/garage-sim:1.0.0

```

## Requisitos Funcionais

O sistema deve importar os dados geolocalizados de vagas e setores e armazenar em um banco de dados.
Vagas podem ter metadados associados como preço, datas, horário de funcionamento e duração da estádia (Documentados
abaixo na API do simulador).

Crie um sistema que gerência o uso e faturamento deste setor de estacionamento, não precisamos de UI,
somete o backend e api REST.

Os dados da garagem são obtidos pelo endpoint de ```GET``` ```/garage``` e 
deve estar apto a receber por webhook entradas e saidas dos veículos por JSON com a API descrita abaixo.

Uma garagem é composta por uma ou mais cancelas automáticas, utilizadas para entrada e saida de veículos e sensores 
de presença nas vagas que analisam a presença ou ausência de um veículo naquela posição.

Após uma chamada ao endpoint de configuração da garagem ```GET``` ```/garage``` a garagem é aberta e o sistema é liberado para entrada e saída de veículos, veja que podemos ter mais de uma cancela automática no mesmo estacionamento ou por setor.

Mesmo para sensores de solo que podem ser acionados em conjunto se varios carros estacionarem ao mesmo tempo.

Assuma que o pagamento é realizado na saida do veiculo, onde o sistema calcula o valor a ser pago pelo cliente.

## Regras de negócio

### Regra de preço dinâmico.

1. Com lotação menor que 25%, desconto de 10% no preço, na hora da entrada.
2. Com lotação menor até 50%, desconto de 0% no preço, na hora da entrada.
3. Com lotação menor até 75%, aumentar o preço em 10%, na hora da entrada.
4. Com lotação menor até 100%, aumentar o preço em 25%, na hora da entrada.

### Regra de lotação
Com 100% de lotação, fechar o setor e só permitir mais carros com a saida de um já estacionado.

## O que vamos avaliar?
O que será avaliado: interpretação dos requisitos, clareza de código, testes, estrutura, escalabilidade, domínio da 
linguagem, aderência aos requisitos, melhorias propostas e cobertura das regras de negócio.

## Processo de seleção

* 2 semanas, para construir o projeto (nosso time demorou em torno de 8 horas para terminar.)
* Entrevista de avaliação do projeto
* Entrevista com RH
* Proposta

------------------
# API - Simulator Webhook ESTAPAR
Sua app deve aceitar conexões pelo url http://localhost:3003/webhook

## Webhook
### Entrada na garagem

**WEBHOOK - POST**
```JSON
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

------------------

### Entrada na vaga

**WEBHOOK - POST**
```JSON
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

------------------

### Saida de garagem

**WEBHOOK - POST**
```JSON
{		
  "license_plate": "",
  "exit_time": "2025-01-01T12:00:00.000Z",
  "event_type": "EXIT"
}
```

## Simulator REST API

### Garage config

**GET**
`/garage`

```JSON
{
  "garage": [
    {
      "sector": "A",
      "basePrice": 10.0,
      "max_capacity": 100,
      "open_hour": "08:00",
      "close_hour": "22:00",
      "duration_limit_minutes": 240
    },
    {
      "sector": "B",
      "basePrice": 4.0,
      "max_capacity": 72,
      "open_hour": "05:00",
      "close_hour": "18:00",
      "duration_limit_minutes": 120
    },
    .
    .
    .
  ],
  "spots": [
    {
      "id": 1,
      "sector": "A",
      "lat": -23.561684,
      "lng": -46.655981
    },
    {
      "id": 2,
      "sector": "B",
      "lat": -23.561674,
      "lng": -46.655971
    },
    .
    .
    .
  ]
}
```

# API do Projeto a ser implementada

## REST API

### Consulta de Placa

**POST**
`/plate-status`
```JSON
{
  "license_plate": "ZUL0001"
}
```

Response
```JSON
{
  "license_plate": "ZUL0001",
  "price_until_now": 0.00,
  "entry_time": "2025-01-01T12:00:00.000Z", 
  "time_parked": "2025-01-01T12:00:00.000Z",
  "lat": -23.561684,
  "lng": -46.655981
}
```

------------------

### Consulta de Vaga

**POST**
`/spot-status`

Request
```JSON
{
  "lat": -23.561684,
  "lng": -46.655981
}
```

Response - 200
```JSON
{
  "ocupied": false,
  "license_plate": "",
  "price_until_now": 0.00,
  "entry_time": "2025-01-01T12:00:00.000Z",
  "time_parked": "2025-01-01T12:00:00.000Z"
}
```

### Consulta faturamento

**GET**
`/revenue`

Request
```JSON
{
  "date": "2025-01-01",
  "sector": "A"
}
```

Response
```JSON
{
  "amount": 0.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T12:00:00.000Z"
}
```



```````
curl http://localhost:3000/garage
mvnw.cmd spring-boot:run
curl -X POST http://localhost:3003/admin/import-garage
curl -X GET http://localhost:3003/revenue -H "Content-Type: application/json" -d "{\"date\": \"2025-06-26\", \"sector\": \"A\"}"
curl -X GET http://localhost:3003/revenue -H "Content-Type: application/json" -d "{\"date\": \"2025-06-26\", \"sector\": \"B\"}"


