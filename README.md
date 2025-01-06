# Sistema de Gerenciamento de Usuários

Sistema de gerenciamento hierárquico de usuários com três níveis: Super Admin, Admin e Usuário Comum.

## Autora

Sophie Alonso

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.0
- H2 Database
- Vue.js 3
- TailwindCSS
- Maven

## Funcionalidades

### Super Admin
- Criar/remover administradores
- Definir limites de usuários por admin
- Remover qualquer usuário
- Visualizar relatórios do sistema

### Admin
- Criar/remover usuários
- Gerenciar usuários dentro do limite estabelecido
- Visualizar seus usuários

### Usuário Comum
- Acesso básico ao sistema

## Configuração

1. Clone o repositório
```bash
git clone [url-do-repositorio]
```

2. Configure o Java 21
3. Importe o projeto na IDE como projeto Maven
4. Execute `mvn clean install`

## Execução

```bash
mvn spring-boot:run
```

Acesse:
- Dashboard Super Admin: http://localhost:8080/dashboard.html
- Painel Admin: http://localhost:8080/admin-panel.html

## Estrutura do Projeto

```
src/main/
├── java/com/sistema/
│   ├── controller/    # Controllers REST
│   ├── model/        # Entidades
│   ├── repository/   # Repositórios JPA
│   ├── service/      # Lógica de negócio
│   ├── exception/    # Exceções customizadas
│   └── util/         # Classes utilitárias
└── resources/
    ├── static/       # Frontend
    └── application.properties
```

## Banco de Dados

H2 Database em arquivo:
- URL: jdbc:h2:file:./dados_sistema
- Console: http://localhost:8080/h2-console
