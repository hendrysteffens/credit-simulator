# 💳 Credit Simulator

Projeto desenvolvido em **Kotlin** com **Spring WebFlux**, utilizando **Arquitetura Hexagonal (Ports and Adapters)** e boas práticas de separação de responsabilidades.  
A aplicação permite simulações de crédito tanto de forma **individual** (síncrona) quanto **em lote** (assíncrona), com rotas funcionais e documentação via **Swagger/OpenAPI**.

---

## 🏗 Arquitetura

O projeto segue o padrão **Hexagonal Architecture**, com a seguinte estrutura:

### **Adapters**
- **credit-rules**  
  Simula uma possível consulta externa para regras de negócio, como serviços **DMN** ou **Machine Learning** para definição de taxas de juros.
- **flyway**  
  Configuração de migrações de banco de dados (setup inicial e futuras atualizações).
- **http**  
  Rotas funcionais (`coRouter`) do Spring WebFlux para comunicação não bloqueante, integradas com **Swagger/OpenAPI**.
- **messaging**  
  Implementação *fake* simulando processamento assíncrono via mensageria.
- **r2dbc**  
  Configuração do banco de dados **Postgres** usando o plugin **TimescaleDB** para limpeza automática de dados a cada dois dias  
  *(entendimento: após a consulta, os dados podem ser descartados, mantendo o banco otimizado)*.

### **Application**
- **Commands**
    - `SingleSimulationCommand` → Processa simulações **síncronas** sem persistência.
    - `SimulationBatchCommand` → Processa múltiplas simulações **assíncronas** em paralelo com `supervisorScope` para evitar falhas globais quando há erros individuais.
- **Queries**
    - `SimulationsByRequestIdQueryHandler` → Consulta paginada de resultados de simulações em lote.

> **Nota:** A ideia inicial incluía uma tabela de `request` para agrupar simulações e permitir *retries* e também enviar uma notificacao apos terminar o processamento assinc, mas foi retirada do escopo por restrição de tempo.

### **Domain**
- **Ports** (interfaces e contratos para injeção de dependência entre módulos).
- **Simulation** → Agregado principal da feature, contendo as **regras de domínio** para o cálculo de parcelas fixas (**PMT**).

---

## 📦 Módulo `deployments`
Utiliza **Kotlin DSL** para inicialização de beans, permitindo configurar diferentes contextos (ex.: leitura e escrita com **CQRS**), subindo apenas os beans necessários para cada pod.

---

## 🚀 Execução local

### **Pré-requisitos**
- Java 17
- Docker

### **Passos**
1. **Subir o banco:**
   ```bash
   docker-compose up -d
   ```
2. **Executar migrações Flyway:**
   ```bash
   ./gradlew flywayMigrate
   ```
3. **Iniciar a aplicação:**
    - Rodar a `main` da classe `CreditsimulatorApplication`.

4. **Acessar a documentação Swagger:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

---

## ✅ Pontos implementados
- Arquitetura Hexagonal completa.
- Processamento síncrono e assíncrono de simulações.
- Rotas funcionais no Spring WebFlux.
- Persistência reativa com R2DBC e Postgres/TimescaleDB.
- Documentação automática via Swagger/OpenAPI.
- Testes unitários básicos para regras de domínio (taxa por idade, cálculo PMT) e algumas rotas.
- Testes de integração simples com banco local.

---

## 🔜 Pontos de melhoria
- Configuração Kubernetes para deploy local com Minikube.
- Regras de negócio mais robustas (validação de parâmetros, exceções de negócio).
- Mais testes unitários e de integração.
- Implementação da tabela `request` para gerenciar processamento de lotes, *retries* e notificação de finalização.
- Adicionar logs e tratamento de erros.
- Criar cache (Ideial seria um cache distribuido como redis em ambiente produtivo)