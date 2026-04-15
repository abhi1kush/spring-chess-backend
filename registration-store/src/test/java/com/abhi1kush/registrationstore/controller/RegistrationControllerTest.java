package com.abhi1kush.registrationstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.exception.RegistrationValidationException;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

	@Mock
	private RegistrationService registrationService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		RegistrationController controller = new RegistrationController(registrationService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void createReturnsValidationErrorForInvalidPayload() throws Exception {
		when(registrationService.create(any()))
				.thenThrow(new RegistrationValidationException("VALIDATION_ERROR", "Validation failed",
						java.util.Map.of("registrationNo", "must not be blank", "currentSemCourses", "must not be empty")));
		mockMvc.perform(post("/api/v1/registrations")
				.contentType(APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("0"))
				.andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.fieldErrors.registrationNo").exists())
				.andExpect(jsonPath("$.fieldErrors.currentSemCourses").exists());
	}

	@Test
	void createReturnsCreatedForValidPayload() throws Exception {
		when(registrationService.create(any())).thenReturn(new RegistrationResponse("1", "", "", "ref-123"));

		mockMvc.perform(post("/api/v1/registrations")
				.contentType(APPLICATION_JSON)
				.content(validRequestJson("sf4t4e354dff20")))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("1"))
				.andExpect(jsonPath("$.errorCode").value(""))
				.andExpect(jsonPath("$.errorDesc").value(""))
				.andExpect(jsonPath("$.refNo").value("ref-123"));
	}

	@Test
	void createReturnsConflictForDuplicateRegistration() throws Exception {
		when(registrationService.create(any())).thenThrow(new DuplicateKeyException("duplicate"));

		mockMvc.perform(post("/api/v1/registrations")
				.contentType(APPLICATION_JSON)
				.content(validRequestJson("sf4t4e354dff21")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value("0"))
				.andExpect(jsonPath("$.errorCode").value("DUPLICATE_REGISTRATION"));
	}

	@Test
	void createReturnsInternalErrorForCustomRegistrationException() throws Exception {
		when(registrationService.create(any()))
				.thenThrow(new RegistrationPersistenceException("PERSISTENCE_ERROR", "Unable to persist registration request", null));

		mockMvc.perform(post("/api/v1/registrations")
				.contentType(APPLICATION_JSON)
				.content(validRequestJson("sf4t4e354dff22")))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value("0"))
				.andExpect(jsonPath("$.errorCode").value("PERSISTENCE_ERROR"));
	}

	@Test
	void createRejectsSuspiciousSqlInput() throws Exception {
		when(registrationService.create(any()))
				.thenThrow(new RegistrationValidationException("SECURITY_VALIDATION_ERROR",
						"Suspicious input detected", java.util.Map.of()));
		mockMvc.perform(post("/api/v1/registrations")
				.contentType(APPLICATION_JSON)
				.content(validRequestJson("sf4t4e354dff31").replace("\"university\":\"university\"",
						"\"university\":\"UNION SELECT\"")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("0"))
				.andExpect(jsonPath("$.errorCode").value("SECURITY_VALIDATION_ERROR"));
	}

	private static String validRequestJson(String registrationNo) {
		return """
				{
				  "registraionNo":"%s",
				  "rollNo":"2342423535",
				  "university":"university",
				  "college":"My college",
				  "firstName":"Abhishek",
				  "lastName":"Singh",
				  "email":"abhi@example.com",
				  "phone":"9876543210",
				  "department":"Computer Science",
				  "batchYear":"2026",
				  "semester":"6",
				  "city":"Delhi",
				  "state":"Delhi",
				  "country":"India",
				  "pincode":"110001",
				  "addressLine1":"Street 1",
				  "addressLine2":"Block A",
				  "dob":"2001-01-15",
				  "gender":"MALE",
				  "idType":"AADHAR",
				  "idNumber":"123412341234",
				  "sourceSystem":"chess_frontend",
				  "referenceCode":"ref001",
				  "current-sem-courses":[
					{
					  "courseCode":"CA101",
					  "corseName":"Computation Theory",
					  "Units":[
						{"name":"DFA","weitage":20},
						{"name":"Turning machine","weitage":10}
					  ]
					}
				  ]
				}
				""".formatted(registrationNo);
	}
}
