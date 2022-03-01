# Comunicação entre Microsserviços

Repositório contendo o projeto desenvolvido do curso Comunicação entre Microsserviços, ministrado por Victor Hugo Negrisoli na Udemy.

## Tecnologias Utilizadas

* **Java 11**
* **Spring Boot**
* **Javascript ES6**
* **Node.js 14**
* **ES6 Modules**
* **Express.js**
* **MongoDB (Container e Cloud MongoDB)**
* **API REST**
* **PostgreSQL (Container e Heroku Postgres)**
* **RabbitMQ (Container e CloudAMQP)**
* **Docker**
* **docker-compose**
* **JWT**
* **Spring Cloud OpenFeign**
* **Axios**
* **Heroku**
* **Coralogix Logging**
* **Kibana**


## APIs

* **Auth-API**: Responsável por gerar um token de acesso ao usuário, que será utilizado nas outras APIs
* **Sales-API**: Responsável por gerenciar as vendas realizadas.
* **Product-API**: Responsável por gerenciar o estoque da aplicação, armazenando os produtos, fornecedores e categoria


## Tracing

* Foi desenvolvido um modo de podermos rastrear as requisições. Onde o "transactionid" transita entre as aplicações e o "serviceid" é interno das aplicações. Para isso deve-se informar um "transactionid" em todas reqs

## Heroku

As 3 APIs foram publicadas no Heroku

* Auth-API    - https://curso-microsservicos-auth-api.herokuapp.com/
* Product-API - https://curso-microsservicos-prod-api.herokuapp.com/
* Sales-API   - https://curso-microsservicos-sales-api.herokuapp.com/


## Execução docker-compose

O seguinte comando pode ser usado para startar as aplicações:

* `docker-compose up --build`
