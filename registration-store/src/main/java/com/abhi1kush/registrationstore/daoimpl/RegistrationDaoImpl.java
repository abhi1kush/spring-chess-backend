package com.abhi1kush.registrationstore.daoimpl;

import java.util.Map;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {
	private static final Logger log = LoggerFactory.getLogger(RegistrationDaoImpl.class);

	private static final String INSERT_SQL_POSTGRESQL = """
			INSERT INTO immutable_registration_requests (
				request_id, registration_no, roll_no, university, college, source_system, request_payload, created_at
			) VALUES (
				:requestId, :registrationNo, :rollNo, :university, :college, :sourceSystem, CAST(:payloadJson AS jsonb), :createdAt
			)
			""";
	private static final String INSERT_SQL_ORACLE = """
			INSERT INTO immutable_registration_requests (
				request_id, registration_no, roll_no, university, college, source_system, request_payload, created_at
			) VALUES (
				:requestId, :registrationNo, :rollNo, :university, :college, :sourceSystem, :payloadJson, :createdAt
			)
			""";

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final String insertSql;

	public RegistrationDaoImpl(
			NamedParameterJdbcTemplate jdbcTemplate,
			@Value("${app.db.vendor:postgresql}") String dbVendor) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertSql = "oracle".equalsIgnoreCase(dbVendor) ? INSERT_SQL_ORACLE : INSERT_SQL_POSTGRESQL;
	}

	@Override
	public void insert(RegistrationEntity entity) {
		Map<String, Object> values = Map.of(
				"requestId", entity.requestId(),
				"registrationNo", entity.registrationNo(),
				"rollNo", entity.rollNo(),
				"university", entity.university(),
				"college", entity.college(),
				"sourceSystem", entity.sourceSystem(),
				"payloadJson", entity.payloadJson(),
				"createdAt", Timestamp.from(entity.createdAt()));
		try {
			jdbcTemplate.update(insertSql, new MapSqlParameterSource(values));
		} catch (DuplicateKeyException e) {
			log.warn("Duplicate registration detected requestId={} registrationNo={}",
					entity.requestId(), entity.registrationNo(), e);
			throw e;
		} catch (DataAccessException e) {
			log.error("Database insert failed requestId={} registrationNo={}",
					entity.requestId(), entity.registrationNo(), e);
			throw new RegistrationPersistenceException(
					"PERSISTENCE_ERROR",
					"Unable to persist registration request",
					e);
		}
	}
}
