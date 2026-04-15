CREATE TABLE immutable_registration_requests (
	request_id VARCHAR2(36) PRIMARY KEY,
	registration_no VARCHAR2(32) NOT NULL UNIQUE,
	roll_no VARCHAR2(20) NOT NULL,
	university VARCHAR2(120) NOT NULL,
	college VARCHAR2(120) NOT NULL,
	source_system VARCHAR2(50) NOT NULL,
	request_payload CLOB CONSTRAINT chk_irr_payload_json CHECK (request_payload IS JSON),
	created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_irr_created_at ON immutable_registration_requests (created_at);
CREATE INDEX idx_irr_source_system ON immutable_registration_requests (source_system);
CREATE INDEX idx_irr_university ON immutable_registration_requests (university);
