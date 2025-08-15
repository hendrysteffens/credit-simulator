#  ADR-002: Comunicação Síncrona com Circuit Breaker
**Contexto**:  
- Exige latência baixa 
- Serviços externos (pricing/anti-fraude) com SLA variável  

**Padrões Implementados**:  
1. Circuit Breaker (sliding window de 60s)  
2. TimeLimiter (300ms hard timeout)  
3. Bulkhead (thread pool dedicado)  
4. Fallback hierárquico:  
   - Cache local (5s TTL)  
   - Regras estáticas  
   - Mock controlado (feature flag)  
