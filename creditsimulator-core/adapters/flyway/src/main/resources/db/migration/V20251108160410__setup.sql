-- Habilita extensão uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- Habilita extensão TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- Tabela de simulations
CREATE TABLE simulation (
    simulation_id UUID,
    request_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    birthdate DATE,
    amount_of_months INT,
    loan_amount NUMERIC(12,2) NOT NULL,
    total_amount NUMERIC(12,2),
    monthly_payment NUMERIC(12,2),
    total_interest NUMERIC(12,2),
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (simulation_id, created_at)
);

-- Converte para hypertable
SELECT create_hypertable('simulation', 'created_at', if_not_exists => TRUE);

-- Política de retenção automática (apaga dados após 2 dias)
SELECT add_retention_policy('simulation', INTERVAL '2 days');