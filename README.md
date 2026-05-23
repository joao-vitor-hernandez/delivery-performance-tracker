# 📦 Delivery Performance Tracker (Monitor de Metas)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Layered%20(MVC%20pattern)-blue)
![UI Design](https://img.shields.io/badge/UI%2FUX-Figma-hotpink)
![Status](https://img.shields.io/badge/Status-Active-success)

A secure, database-driven Java application designed to help delivery drivers track, analyze, and recover their delivery performance based on real-world logistics metrics.

---

## 🚀 Overview

This project simulates a real logistics scenario where delivery performance must reach a **98% success rate** (Platinum Level). Originally a basic script, it has evolved into a robust, secure, multi-user system powered by a relational database and cryptographic security.

The system allows users to:
- **Secure Authentication:** Create accounts and log in securely with password encryption.
- **Isolated Driver Data:** Multi-user support ensures each driver only views and manages their own delivery history.
- **Smart Tracking:** Register or update daily deliveries (successes/failures). If an entry already exists for a given date, the system intelligently updates it instead of creating duplicates.
- **Mathematical Predictor:** Dynamically calculate monthly performance and project exactly how many perfect deliveries are needed to reach the 98% target.
- **Professional PDF Reports:** Export detailed monthly statistics into a well-formatted PDF document.

---

## 🎨 UI/UX Design (Mobile Concept)
To envision the project beyond the CLI (Command Line Interface), a high-fidelity prototype was developed using **Figma**. The design focuses on **Dark Mode** for driver comfort during night shifts and ergonomic, single-handed usability in the field.

**[🔗 Click here to view the Interactive Figma Prototype](#)** *(https://www.figma.com/proto/Nq7fWFdKD26ISy8yL7Wquj/delivery-performance-tracker?node-id=16-84&p=f&viewport=1308%2C743%2C0.82&t=nJSqF7z83uk3FQB7-1&scaling=min-zoom&content-scaling=fixed&starting-point-node-id=1%3A1746&page-id=0%3A1)*

### Key Features & UX Decisions:
- **Dynamic Dashboard States:** The main screen adapts visually based on the driver's performance, showing different UI states for "Target Reached" (≥ 98%) versus "Recovery Needed" (< 98%).
- **Floating Action Menu:** Implemented on the main tab to provide quick access to core actions without cluttering the screen.
- **Integrated Stopwatch:** A brand new screen designed specifically for drivers to track their route times, addressing a real-world time management need.
- **Refined Data Flows:** Updated, frictionless interfaces for Login, History viewing, and daily Entry logging.

| Login Screen | Dashboard (Target Reached) | Dashboard (Recovery Needed) |
| :---: | :---: | :---: |
| ![Login](assets/loginAtt.png) | ![Home Success](assets/principalAtt.png) | ![Home Alert](assets/principalAtt2.png) |

| New Entry | History Log | Route Stopwatch |
| :---: | :---: | :---: |
| ![Entry](assets/novaEntregaAtt.png) | ![History](assets/historicoAtt.png) | ![Stopwatch](assets/cronometro.png) |

---

## 🏗️ Software Architecture

The project follows a solid **Layered Architecture** with clean separation of concerns:

- **Model (`Usuario.java`, `Entrega.java`):** Represents domain entities. Uses Restrictive Encapsulation (Fail-Fast principle) to block invalid inputs, such as negative package counts, at the constructor level.
- **Repository (`UsuarioRepository.java`, `EntregaRepository.java`, `ConexaoSQLite.java`):** Data Access Object (DAO) layer. Replaced unstable CSV file handling with robust **SQLite database integration via JDBC**, protecting the system against SQL Injection using `PreparedStatement`.
- **Service (`EntregaService.java`, `SenhaService.java`, `RelatorioPdfService.java`):** Core business logic layer. Handles performance metrics, target projections, password hashing using **BCrypt**, and document rendering via **OpenPDF**.
- **UI (`Principal.java`):** The interactive terminal view (CLI). Fully decoupled from business formulas, managing state machine sessions (Logged out / Logged in menus) and inputs safely.

---

## 🛠️ Technologies & Best Practices

- **Java SE** (Stream API, LocalDate, Lambda expressions)
- **Dependency Management:** Apache Maven (`pom.xml`)
- **SQLite Database** & JDBC (Java Database Connectivity)
- **BCrypt Hashing:** Secure password storage using strong salted hashing algorithms (**Privacy by Design / LGPD Aligned**).
- **OpenPDF / Openhtmltopdf:** Professional generation of dynamic PDF reports.
- **Defensive Programming:** Robust exception handling preventing crashes from bad terminal inputs (`InputMismatchException`, `DateTimeParseException`) or SQL states.
- **Clean Code & OOP:** Focus on the DRY (Don't Repeat Yourself) principle and single-responsibility components.

---

## ▶️ How to Run

This project uses **Apache Maven** for automated dependency management and build execution. You do not need to download external `.jar` files manually.

### Prerequisites
- Java JDK 21 or higher
- Apache Maven installed

### Execution Steps
```bash
# 1. Clone the repository
git clone [[https://github.com/joao-vitor-hernandez/delivery-performance-tracker.git](https://github.com/joao-vitor-hernandez/delivery-performance-tracker.git)]
cd delivery-performance-tracker

# 2. Compile and download dependencies
mvn clean compile

# 3. Run the application
mvn exec:java -Dexec.mainClass="Principal"
```

---

## 📊 Example Output
--- MONTHLY PERFORMANCE SUMMARY ---
Driver: joao
Total Packages Handled: 150
Current Success Rate: 96.00%

⚠ ALERT: You need 15 perfect deliveries to recover your Platinum Level (98%).
----------------------------------
[System] PDF Report successfully generated: Relatório_Entregas_joaovitor_5_2026.pdf

---

## 🎯 Purpose

This project was built to solve a real-world problem faced by delivery drivers, applying programming concepts to practical scenarios.

---

## 🚧 Roadmap & Next Steps
- [x] Refactor architecture (Separation of Concerns).
- [x] Migrate from CSV files to a relational local Database (SQLite).
- [x] Integrate Apache Maven for standard dependency management (pom.xml).
- [x] Privacy by Design: Secure authentication with BCrypt hashing (LGPD compliance).
- [x] Implement duplicate prevention (Smart SQL UPSERT/Update logic).
- [x] Implement professional Document Exporting (PDF Service).
- [x] UI/UX Mobile Prototyping (Figma).
- [ ] Implement Unit Testing with JUnit to validate core business logic and metrics.
- [ ] Build an API layer (Spring Boot REST API).
- [ ] Develop the Mobile Frontend (Android / Flutter).

---

## 👨‍💻 Author

**João Vitor Hernandez** [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/jo%C3%A3o-vitor-hernandez-dev-java)

---

---

## 🇧🇷 Versão em Português

### 📦 Monitor de Metas - Logística
Uma aplicação em Java profissional criada para ajudar entregadores de logística a rastrear, analisar e recuperar sua performance de entregas baseada na meta corporativa de 98% de sucesso (Nível Platina).

### 🛡️ O que mudou na nova versão?
O projeto deixou de ser um script local que lia arquivos de texto básicos (CSV) e se tornou um ecossistema seguro e corporativo:
- Gerenciamento de Dependências Moderno: Migração completa para o Apache Maven, centralizando bibliotecas via pom.xml.
- Segurança de Dados (Alinhado à LGPD): Implementação de controle de usuários com senhas criptografadas via algoritmo BCrypt (salvamento em formato Hash utilizando conceito de Privacy by Design).
- Banco de Dados Relacional: Integração total com SQLite via JDBC, garantindo persistência robusta, integridade de dados e proteção ativa contra SQL Injection usando PreparedStatement.
- Ambiente Multiusuário: Dados totalmente isolados. Cada motorista só visualiza e edita seu próprio histórico de entregas.
- Lógica Inteligente: Sistema integrado de atualização diária. Registrar novos dados em uma data existente atualiza o registro atual automaticamente.
- Relatórios em PDF: Módulo de exportação automatizada para geração de relatórios mensais diagramados e profissionais.

#### 🎨 Design UI/UX
O projeto evoluiu para um conceito mobile moderno em Dark Mode, desenhado no Figma com foco em usabilidade prática e ergonômica para o dia a dia na rua.

🔗 Clique aqui para acessar o Protótipo Interativo no Figma (https://www.figma.com/proto/Nq7fWFdKD26ISy8yL7Wquj/delivery-performance-tracker?node-id=16-84&p=f&viewport=1308%2C743%2C0.82&t=nJSqF7z83uk3FQB7-1&scaling=min-zoom&content-scaling=fixed&starting-point-node-id=1%3A1746&page-id=0%3A1)

Destaques da Interface:
- Dashboard Dinâmico: Telas principais que se adaptam visualmente caso o motorista esteja acima ou abaixo da meta de 98% (Alertas visuais).
- Menu Flutuante (FAB): Adicionado à tela principal para facilitar a navegação rápida e com apenas uma mão durante a rotina de entregas.
- Cronômetro Integrado (Novo): Uma ferramenta nativa para o motorista medir o tempo de suas rotas, agregando valor direto à gestão de tempo operacional.
- Telas Atualizadas: Refinamento visual nas telas de Login, Histórico e Nova Entrega, garantindo uma jornada de usuário sem atritos.

#### 🏗️ Arquitetura e Engenharia
- **Model:** Entidades puras (Usuario e Entrega) protegidas por encapsulamento restritivo (princípio Fail-Fast).
- **Service:** Concentração das regras de negócio, criptografia (SenhaService), projeções matemáticas de recuperação de metas (EntregaService) e renderização de documentos (RelatorioPdfService).
- **Repository:** Camada de persistência (ConexaoSQLite, UsuarioRepository, EntregaRepository) focada em operações ACID seguras no banco de dados.
- **UI:** Terminal interativo estruturado em máquina de estados para gerenciar o fluxo antes e depois da autenticação do motorista.

#### 🚧 Próximos Passos Cadastrados no Roadmap
- Criação de testes unitários automatizados com JUnit para blindar a lógica dos cálculos matemáticos de metas.
- Transição da camada de controle para uma arquitetura de microsserviços/API REST utilizando Spring Boot.