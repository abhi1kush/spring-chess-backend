package com.abhi1kush.registrationstore.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.exception.RegistrationSerializationException;
import com.abhi1kush.registrationstore.valuebeans.CourseBean;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;
import com.abhi1kush.registrationstore.valuebeans.UnitBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

	@Mock
	private RegistrationDao registrationDao;

	@Mock
	private ObjectMapper objectMapper;

	private RegistrationServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new RegistrationServiceImpl(registrationDao, objectMapper);
	}

	@Test
	void createStoresDataAndReturnsSuccessResponse() throws Exception {
		RegistrationRequest request = validRequest("sf4t4e354dff23");
		when(objectMapper.writeValueAsString(any())).thenReturn("{\"ok\":true}");

		RegistrationResponse response = service.create(request);

		assertThat(response.status()).isEqualTo("1");
		assertThat(response.errorCode()).isEmpty();
		assertThat(response.errorDesc()).isEmpty();
		assertThat(response.refNo()).isNotBlank();

		ArgumentCaptor<RegistrationEntity> captor = ArgumentCaptor.forClass(RegistrationEntity.class);
		verify(registrationDao).insert(captor.capture());
		RegistrationEntity saved = captor.getValue();
		assertThat(saved.registrationNo()).isEqualTo("sf4t4e354dff23");
		assertThat(saved.payloadJson()).isEqualTo("{\"ok\":true}");
	}

	@Test
	void createThrowsSerializationExceptionWhenJsonConversionFails() throws Exception {
		RegistrationRequest request = validRequest("sf4t4e354dff24");
		when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("boom") {
			private static final long serialVersionUID = 1L;
		});

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(RegistrationSerializationException.class)
				.hasMessageContaining("Unable to serialize");
	}

	@Test
	void createPropagatesPersistenceException() throws Exception {
		RegistrationRequest request = validRequest("sf4t4e354dff25");
		when(objectMapper.writeValueAsString(any())).thenReturn("{}");
		doThrow(new RegistrationPersistenceException("PERSISTENCE_ERROR", "Unable to persist registration request", null))
				.when(registrationDao).insert(any());

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(RegistrationPersistenceException.class)
				.hasMessageContaining("Unable to persist");
	}

	private static RegistrationRequest validRequest(String registrationNo) {
		RegistrationRequest request = new RegistrationRequest();
		request.setRegistrationNo(registrationNo);
		request.setRollNo("2342423535");
		request.setUniversity("university");
		request.setCollege("My college");
		request.setFirstName("Abhishek");
		request.setLastName("Singh");
		request.setEmail("abhi@example.com");
		request.setPhone("9876543210");
		request.setDepartment("Computer Science");
		request.setBatchYear("2026");
		request.setSemester("6");
		request.setCity("Delhi");
		request.setState("Delhi");
		request.setCountry("India");
		request.setPincode("110001");
		request.setAddressLine1("Street 1");
		request.setAddressLine2("Block A");
		request.setDob("2001-01-15");
		request.setGender("MALE");
		request.setIdType("AADHAR");
		request.setIdNumber("123412341234");
		request.setSourceSystem("chess_frontend");
		request.setReferenceCode("ref001");

		UnitBean unit1 = new UnitBean();
		unit1.setName("DFA");
		unit1.setWeitage(20);
		UnitBean unit2 = new UnitBean();
		unit2.setName("Turning machine");
		unit2.setWeitage(10);

		CourseBean course = new CourseBean();
		course.setCourseCode("CA101");
		course.setCorseName("Computation Theory");
		course.setUnits(List.of(unit1, unit2));
		request.setCurrentSemCourses(List.of(course));
		return request;
	}
}
