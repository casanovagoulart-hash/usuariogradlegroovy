# 👤 Microsserviço de Usuários (`usuariogradlegroovy`)

![Java](https://shields.io)
![Spring Boot](https://shields.io)
![Groovy](https://shields.io)
![PostgreSQL](https://shields.io)
![Docker](https://shields.io)

Microsserviço responsável pelo cadastro de usuários, gestão de endereços/telefones, consulta de CEP (ViaCEP), autenticação e emissão de tokens **JWT (HS512)** dentro do ecossistema **Agendador de Tarefas**.

## 🛡️ Segurança e Arquitetura
* **Persistência Relacional:** Spring Data JPA + Hibernate conectados ao **PostgreSQL**.
* **Criptografia de Senhas:** Armazenamento seguro com **BCrypt** (sem exposição de hashes nos DTOs de resposta).
* **Autenticação JWT (HS512):** Geração e validação de tokens assinados via chave compartilhada (`JWT_SECRET` ≥ 512 bits).
* **Isolamento de Rede:** Executa na porta interna `8080`, acessado exclusivamente pelo BFF (`bff-agendador-tarefas`) via rede Docker `backend-net`.

## ⚙️ Variáveis de Ambiente Necessárias
* `SPRING_DATASOURCE_URL`: URL JDBC do PostgreSQL (ex: `jdbc:postgresql://db:5432/db_usuario`)
* `SPRING_DATASOURCE_USERNAME`: Usuário do banco (`postgres`)
* `SPRING_DATASOURCE_PASSWORD`: Senha do PostgreSQL
* `JWT_SECRET`: Chave secreta compartilhada de 128 caracteres Base64

## 🙏 Créditos e Agradecimentos
Desenvolvido com apoio da **Javanauta Academy** (mentora **Angélica Weiler**), assistência arquitetural e pair programming com **IA (Google)**, e dedicado a **Deus** e à minha família (**esposa, filhos, neto, nora e genro**).
