# ADR-001: Persistência de Eventos via PostgreSQL Append-Only
**Contexto**:  
- Necessidade de auditabilidade completa

**Alternativas**:

| Opção |                     Prós                     | Contras |
|:---|:--------------------------------------------------------:|---:|
| MongoDB |               Schema-less, fácil evolução               | Sem transações ACID robustas |
| **PostgreSQL** |                ACID, JSONB, equipe dominante                | Implementação manual de streams |

**Decisão**:  
`PostgreSQL 16 com tabela append-only + snapshots`

**Justificativa**:  
- Redução de complexidade inicial com tecnologia conhecida   
- Custo menor 

**Implicações**:  
- Desenvolver utilitários de rehydrate
