package com.abhi1kush.registrationstore.daoimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;

@ExtendWith(MockitoExtension.class)
class RegistrationDaoImplTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Test
	void insertUsesPostgresqlQueryWhenVendorIsPostgresql() {
		RegistrationDaoImpl dao = new RegistrationDaoImpl(jdbcTemplate, "postgresql");
		RegistrationEntity entity = entity("sf4t4e354dff26");
		when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);

		dao.insert(entity);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(jdbcTemplate).update(sqlCaptor.capture(), any(MapSqlParameterSource.class));
		assertThat(sqlCaptor.getValue()).contains("CAST(:payloadJson AS jsonb)");
	}

	@Test
	void insertUsesOracleQueryWhenVendorIsOracle() {
		RegistrationDaoImpl dao = new RegistrationDaoImpl(jdbcTemplate, "oracle");
		RegistrationEntity entity = entity("sf4t4e354dff27");
		when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);

		dao.insert(entity);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(jdbcTemplate).update(sqlCaptor.capture(), any(MapSqlParameterSource.class));
		assertThat(sqlCaptor.getValue()).doesNotContain("jsonb");
		assertThat(sqlCaptor.getValue()).contains(":payloadJson");
	}

	@Test
	void insertRethrowsDuplicateKeyException() {
		RegistrationDaoImpl dao = new RegistrationDaoImpl(jdbcTemplate, "postgresql");
		RegistrationEntity entity = entity("sf4t4e354dff28");
		doThrow(new DuplicateKeyException("duplicate"))
				.when(jdbcTemplate).update(anyString(), any(MapSqlParameterSource.class));

		assertThatThrownBy(() -> dao.insert(entity))
				.isInstanceOf(DuplicateKeyException.class);
	}

	@Test
	void insertWrapsDataAccessExceptionAsRegistrationPersistenceException() {
		RegistrationDaoImpl dao = new RegistrationDaoImpl(jdbcTemplate, "postgresql");
		RegistrationEntity entity = entity("sf4t4e354dff29");
		doThrow(new DataAccessResourceFailureException("db down"))
				.when(jdbcTemplate).update(anyString(), any(MapSqlParameterSource.class));

		assertThatThrownBy(() -> dao.insert(entity))
				.isInstanceOf(RegistrationPersistenceException.class)
				.hasMessageContaining("Unable to persist");
	}

	private static RegistrationEntity entity(String registrationNo) {
		return new RegistrationEntity(
				"req-1",
				registrationNo,
				"2342423535",
				"university",
				"My college",
				"chess_frontend",
				"{\"sample\":true}",
				Instant.now());
	}
}
