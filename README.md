# Estapar - Sistema de Gestão de Estacionamentos

Sistema desenvolvido como parte do processo seletivo para Desenvolvedor Java/Kotlin Backend da Estapar. O projeto implementa a gestão de entradas, saídas, ocupação e faturamento de um estacionamento, integrando com um simulador via Webhook.

##  Visão Geral

O sistema recebe dados da garagem e dos eventos dos veículos via:

* Endpoint `/garage` (simulador): importa setores e vagas
* Webhooks `/webhook`: recebe eventos `ENTRY`, `PARKED` e `EXIT`
* API REST:

  * `POST /plate-status` - Consulta status por placa
  * `POST /spot-status` - Consulta status por coordenada
  * `GET /revenue`       - Consulta faturamento por setor e data

Aplica regras de negócio de ocupação e preço dinâmico no momento da entrada.

##  Tecnologias Utilizadas

* Java 21
* Spring Boot 3
* Spring Data JPA
* MySQL 8
* Docker e Docker Compose

##  Como Executar Localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/pedritto19/Estapar.git

```

### 2. Subir os (MySQL + App + Simulador)


```bash
    mvnw.cmd spring-boot:run
    docker run --rm --network=host cfontes0estapar/garage-sim:1.0.0
    docker run --name estapar-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=estapar -p 3306:3306 -d mysql:8
```

### 3. Importar configuração da garagem

```bash
curl http://localhost:3000/garage
curl -X POST http://localhost:3003/admin/import-garage
```

### 4. Testar os endpoints

```bash
# Consulta de vaga
curl -X POST http://localhost:3003/spot-status \
     -H "Content-Type: application/json" \
     -d '{"lat": -23.561684, "lng": -46.655981}'

# Consulta de placa
curl -X POST http://localhost:3003/plate-status \
     -H "Content-Type: application/json" \
     -d '{"license_plate": "ZUL0001"}'

# Consulta de faturamento
curl -X GET "http://localhost:3003/revenue?date=2025-06-26&sector=A"
```

##  Estrutura de Pastas

```
src/main/java/com/estapar/parking/
├── controller         # Endpoints REST
├── model/dto                # Objetos de requisicao/resposta
├── model/entity             # Entidades JPA (Sector, Spot, VehicleEntry)
├── repository         # Repositórios Spring Data
├── service            # Regras de negócio e processamentos
└── EstaparParkingApplication.java
```

##  Endpoints REST

### `POST /plate-status`

Consulta status de um veículo por placa.

### `POST /spot-status`

Consulta status de ocupação de uma vaga por coordenada.

### `GET /revenue`

Consulta o faturamento acumulado de um setor em determinada data.

Exemplo:

```bash
curl -X GET "http://localhost:3003/revenue?date=2025-06-26&sector=A"
```

##  Regras de Negócio

### Ocupação Dinâmica:

* < 25%: desconto de 10%
* < 50%: preço normal
* < 75%: acréscimo de 10%
* <100%: acréscimo de 25%

### Lotação:

* 100% de ocupação: entrada não permitida

##  Requisitos Atendidos

* [x] Integração com simulador
* [x] Armazenamento de setores, vagas e entradas
* [x] Controle de ocupação e liberação de vagas
* [x] Preço dinâmico conforme lotação
* [x] Webhook com entrada, vaga ocupada e saída
* [x] API REST funcional com exemplos testados


---

> Desenvolvido como parte do desafio técnico da Estapar para backend com Spring Boot.
