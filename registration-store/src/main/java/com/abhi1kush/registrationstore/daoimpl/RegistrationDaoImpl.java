package com.abhi1kush.registrationstore.daoimpl;

import java.util.Map;
import java.sql.Timestamp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {
	private static final Logger log = LoggerFactory.getLogger(RegistrationDaoImpl.class);
	private static final String SQL_PATH_POSTGRESQL = "sql/registration/insert-registration-postgresql.sql";
	private static final String SQL_PATH_ORACLE = "sql/registration/insert-registration-oracle.sql";

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final String insertSql;

	public RegistrationDaoImpl(
			NamedParameterJdbcTemplate jdbcTemplate,
			@Value("${app.db.vendor:postgresql}") String dbVendor) {
		this.jdbcTemplate = jdbcTemplate;
		String sqlPath = "oracle".equalsIgnoreCase(dbVendor) ? SQL_PATH_ORACLE : SQL_PATH_POSTGRESQL;
		this.insertSql = loadSql(sqlPath);
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

	private String loadSql(String sqlPath) {
		try {
			ClassPathResource resource = new ClassPathResource(sqlPath);
			return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).trim();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load SQL from classpath: " + sqlPath, e);
		}
	}
}
