CREATE TABLE IF NOT EXISTS immutable_registration_requests (
	request_id VARCHAR(36) PRIMARY KEY,
	registration_no VARCHAR(32) NOT NULL UNIQUE,
	roll_no VARCHAR(20) NOT NULL,
	university VARCHAR(120) NOT NULL,
	college VARCHAR(120) NOT NULL,
	source_system VARCHAR(50) NOT NULL,
	request_payload JSONB NOT NULL,
	created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_irr_created_at ON immutable_registration_requests (created_at);
CREATE INDEX IF NOT EXISTS idx_irr_source_system ON immutable_registration_requests (source_system);
CREATE INDEX IF NOT EXISTS idx_irr_university ON immutable_registration_requests (university);
CREATE INDEX IF NOT EXISTS idx_irr_payload_gin ON immutable_registration_requests USING GIN (request_payload);
