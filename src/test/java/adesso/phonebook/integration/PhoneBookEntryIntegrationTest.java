package adesso.phonebook.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		mockMvc.perform(get("/api/phonebook_entries"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(20))); // data.json enthält 20 Personen
	}

	@Test
	void filterPersonen_shouldReturnMatchingNames() throws Exception {
		mockMvc.perform(get("/api/phonebook_entries").param("name", "an"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vorname", containsString("An")));
	}

	@Test
	void filterPersonen_shouldBeCaseInsensitive() throws Exception {
		// Lowercase
		mockMvc.perform(get("/api/phonebook_entries").param("name", "an"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));

		// Uppercase
		mockMvc.perform(get("/api/phonebook_entries").param("name", "AN"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));

		// Mixed case
		mockMvc.perform(get("/api/phonebook_entries").param("name", "An"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Anna")));
	}

	@Test
	void createNewPerson_shouldReturnCreatedStatus() throws Exception {
		PhoneBookEntry neuePerson = new PhoneBookEntry();
		neuePerson.setVorname("Lisa");
		neuePerson.setNachname("Maier");
		neuePerson.setTelefonVorwahl("+49");
		neuePerson.setTelefonnummer("1761111222");

		String json = objectMapper.writeValueAsString(neuePerson);

		mockMvc.perform(post("/api/phonebook_entries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated());
	}
}
