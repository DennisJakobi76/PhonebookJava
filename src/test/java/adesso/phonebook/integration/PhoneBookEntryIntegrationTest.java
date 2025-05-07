package adesso.phonebook.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import adesso.phonebook.PhoneBookEntry;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneBookEntryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getAllPersonen_shouldReturn20Items() throws Exception {
		mockMvc.perform(get("/api/phonebook"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(20))); // data.json enthält 20 Personen
	}

	@Test
	void filterPersonen_shouldReturnMatchingNames() throws Exception {
		mockMvc.perform(get("/api/phonebook").param("name", "an"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName", containsString("An")));
	}

	@Test
	void filterPersonen_shouldBeCaseInsensitive() throws Exception {
		// Lowercase
		mockMvc.perform(get("/api/phonebook").param("name", "an"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));

		// Uppercase
		mockMvc.perform(get("/api/phonebook").param("name", "AN"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));

		// Mixed case
		mockMvc.perform(get("/api/phonebook").param("name", "An"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));
	}

	@Test
	void createNewPerson_shouldReturnCreatedStatus() throws Exception {
		PhoneBookEntry neuePerson = new PhoneBookEntry();
		neuePerson.setFirstName("Lisa");
		neuePerson.setLastName("Maier");
		neuePerson.setPhonePrefix("+49");
		neuePerson.setPhoneNumber("1761111222");

		String json = objectMapper.writeValueAsString(neuePerson);

		mockMvc.perform(post("/api/phonebook")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated());
	}

	@Test
	void updatePerson_shouldReturnOkStatus() throws Exception {
		// Existierende Person (ID 1 aus data.json) aktualisieren
		PhoneBookEntry updatePerson = new PhoneBookEntry();
		updatePerson.setFirstName("Anna-Marie");
		updatePerson.setLastName("Schmidt");
		updatePerson.setPhonePrefix("+49");
		updatePerson.setPhoneNumber("1599999999");

		String json = objectMapper.writeValueAsString(updatePerson);

		mockMvc.perform(put("/api/phonebook/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());

		// Überprüfen, ob die Änderungen gespeichert wurden
		mockMvc.perform(get("/api/phonebook/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Anna-Marie"))
				.andExpect(jsonPath("$.lastName").value("Schmidt"))
				.andExpect(jsonPath("$.phoneNumber").value("1599999999"));
	}

	@Test
	void updateNonExistingPerson_shouldReturnNotFound() throws Exception {
		PhoneBookEntry updatePerson = new PhoneBookEntry();
		updatePerson.setFirstName("Test");
		updatePerson.setLastName("Person");
		updatePerson.setPhonePrefix("+49");
		updatePerson.setPhoneNumber("1234567890");

		String json = objectMapper.writeValueAsString(updatePerson);

		mockMvc.perform(put("/api/phonebook/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	void deletePerson_shouldReturnNoContent() throws Exception {
		// Erst einen GET-Request machen um sicherzustellen, dass die Person existiert
		mockMvc.perform(get("/api/phonebook/1"))
				.andExpect(status().isOk());

		// Person löschen
		mockMvc.perform(delete("/api/phonebook/1"))
				.andExpect(status().isNoContent());

		// Überprüfen, ob die Person wirklich gelöscht wurde
		mockMvc.perform(get("/api/phonebook/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteNonExistingPerson_shouldReturnNotFound() throws Exception {
		mockMvc.perform(delete("/api/phonebook/999"))
				.andExpect(status().isNotFound());
	}
}
