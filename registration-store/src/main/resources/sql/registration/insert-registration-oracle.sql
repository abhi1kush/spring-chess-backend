INSERT INTO immutable_registration_requests (
    request_id, registration_no, roll_no, university, college, source_system, request_payload, created_at
) VALUES (
    :requestId, :registrationNo, :rollNo, :university, :college, :sourceSystem, :payloadJson, :createdAt
);
