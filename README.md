# Voting System

Este é um simples sistema de votação. 

A aplicação conta com um sistema de autenticação e autorização que utiliza JWT e baseia as permissões em Roles de usuário. 

Junto a aplicação estão as collections do Postman que serão úteis para testar a aplicação. 
Para facilitar o uso da API o Swagger foi configurado para gerar a documentação.

O Docker foi utilizado para prover as aplicações necessárias para executar a aplicação, tais como: banco de dados (Postgresql), Processador de Streams/Filas (Kafka) entre outros.

Para garantir que nenhum usuário vote sem permissão, foi feito uma integração com um sistema que verifica se o usuário pode votar através do seu CPF.

Para o versionamento da base de dados foi utilizado o Liquibase, ele é responsável por criar a estrutura inicial da base de dados e gerenciar toda a sua evolução posterior.

Foram criados somente testes unitários para a camada de serviço, o ideal seria criar também testes unitários nos controllers com Mock MVC, 
teste integrado para validar as consultas da base de dados (utilizando um banco embarcado como o H2), teste integrado para verificar os 
tópicos do Kafka etc. Infelizmente meu tempo foi mais curto que o esperado e não pude implementá-los.

## Requisitos

* Java JDK 11
* Maven
* Docker
* Docker Compose
* Git

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
mvn spring-boot:run
```

A aplicação estará disponível em: [http://localhost:8080](http://localhost:8080/)

## Instruções de Uso

Com a aplicação em execução, a documentação da API estará disponível em: http://localhost:8080/swagger-ui.html.

Para facilitar o teste da API, um arquivo de Collections e Environment do Postman está disponível no diretório "postman" na raiz do projeto. 
Faça a importação destes arquivos no Postman e execute cada Item da collection conforme os passos a seguir:

#### 1) Collection - Login Admin User
Esta aplicação implementa autenticação e autorização, dessa forma é necessário autenticar-se com o usuário administrador;


####2) Collection - Create Guideline
Crie uma pauta para votação;


#### 3) Collection - Create Session
Crie uma sessão para votação, se preferir, adicione uma data de expiração (consulte documentação do Swagger: http://localhost:8080/swagger-ui.html#/session/createUsingPOST_1);
   

#### 4) Collection - Create User ASSOCIATE
Crie um ou mais usuários Associados para votar, lembre-se que o CPF será validado no momento da votação, portanto insira CPFs válidos. Você poderá gerar CPFs válidos em: https://www.4devs.com.br/gerador_de_cpf. Para criar mais de um usuário será necessário alterar os valores únicos de cada usuário: CPF, email e username;
   

#### 5) Collection - Login Associate User
Faça login com o usuário que você criou para fazer a votação, alterando, caso necessário, os campos da requisição que você modificou;
   

#### 6) Collection - Create Vote
Utilize o usuário logado para votar. Não se preocupe, não é necessário passar o usuário na requisição;
   
#### 7) Repita os passos 4, 5 e 6 para votar com quantos usuário quiser.

#### 8) Collection - Login Admin User 
Faça login como Administrador novamente para verificar como anda o status da votação;

#### 9) Collection Get Session Result
Verifique o status da votação.

Quando a sessão de votação atingir a data e hora de expiração, um Job enviará o resultado para um tópico no Kafka. 
Para facilitar a verificação das mensagens no tópico, o Kafdrop foi configurado e pode ser acessado em: http://localhost:19000. 





