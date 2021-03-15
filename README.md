# Voting System

Este é um simples sistema de votação.

## Requisitos

* Java JDK 11
* Maven
* Docker
* Docker Compose

## Como Executar

Utilizando de preferência o sistema operacional Linux, abra o Terminal. 
Na raiz do projeto (onde se encontra o arquivo pom.xml), siga os passos:

1) Execute o docker-compose para que todas as aplicações que o projeto depende fiquem operantes:

```bash
docker-compose up -d --build
```

2) Faça o build do projeto utilizando o Maven:

```bash
mvn clean install
```

3) Execute o projeto utilizando o Maven:

```bash
mvn spring-boot:Run
```