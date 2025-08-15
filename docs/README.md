# Arquitetura do Sistema de Crédito (DDD + Event Sourcing)

## 1. Contexto Rápido
**Domínio Principal**:  
`Crédito` (agregado `Credito`) - ciclo de vida completo de empréstimos.

**Subdomínios Estratégicos**:
- `Precificação`: Motor de pricing com ML + regras
- `Anti-Fraude`: Avaliação de risco em tempo real
- `Pagamento Recebidos`: Integração com servicos de pagamento
- `Pagamento Enviados`: Integração com servicos de pagamento
- `Notificações`: Comunicação assíncrona com usuários
- `Identidade & Acesso`: Gestão de autenticação/autorização

## 2. Limites de Domínio

| Bounded Context |                     Responsabilidade                     | Padrão Integração |
|:---|:--------------------------------------------------------:|---:|
| Credito (Core) | Ciclo de vida do crédito (abertura, execução, simulação) | Texto |
| Pricing |               Cálculo de taxas e parcelas                | Síncrono (gRPC) |
| Anti-Fraude |                Score de risco e validação                | Síncrono (gRPC) |
| Pagamentos |                Conciliação com PSPs              | Assíncrono (Kafka)|
| Identidade |                Autenticação/OAuth2              | Síncrono (OIDC)|

## 3. Fluxos Principais

### 3.1 Simulação Síncrona Resiliente![fluxo-sinc.svg](fluxo-sinc.svg)

### 3.2 Incluindo fluxo de authZ/AuthN![authZauthN.svg](authZauthN.svg)

### 3.3 Fluxo completo
![fluxo-completo.svg](fluxo-completo.svg)

#### 3.4 Explicação do Fluxo Completo

- Passo 1: Usuário inicia uma simulação via API
- Passo 2: O Credit carrega um novo agregado (vazio) do Event Store
- Passo 3: Serviços externos (Pricing e Anti-Fraude) são consultados
- Passo 4: Evento SimulacaoGerada é armazenado e publicado
- Passo 5: Após simulação concluida, o usuario comanda 'CriarCredito' a partir da sumulacao gerada.
- Passo 5: Valida no dominio e cria a solicitação, se aprovada o sistemas de pagamento reagem.
- Passo 6: Após as solicitação de pagamento authorizado, cria o evento de credito aprovado e finaliza o processo
