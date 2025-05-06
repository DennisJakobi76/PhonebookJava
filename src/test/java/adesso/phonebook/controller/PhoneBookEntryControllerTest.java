package adesso.phonebook.controller;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import adesso.phonebook.PhoneBookEntry;
import adesso.phonebook.PhoneBookEntryController;
import adesso.phonebook.PhoneBookEntryService;

@WebMvcTest(PhoneBookEntryController.class)
public class PhoneBookEntryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PhoneBookEntryService phoneBookEntryService;

	private List<PhoneBookEntry> testDaten;

	@BeforeEach
	@SuppressWarnings("unused")
	void setup() {
		testDaten = List.of(
				new PhoneBookEntry(1L, "Anna", "Schneider", "+49", "1512345678"),
				new PhoneBookEntry(2L, "Andreas", "Müller", "+49", "1523456789"),
				new PhoneBookEntry(3L, "Sandra", "Weber", "+49", "1609876543"));
	}

	@Test
	void testGetAll() throws Exception {
		given(phoneBookEntryService.getAll()).willReturn(testDaten);

		mockMvc.perform(get("/api/phonebook"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	void testFilterByName() throws Exception {
		given(phoneBookEntryService.filterByName("an")).willReturn(List.of(
				testDaten.get(0), // Anna
				testDaten.get(1), // Andreas
				testDaten.get(2) // Sandra
		));

		mockMvc.perform(get("/api/phonebook").param("name", "an"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(content().string(containsString("Anna")))
				.andExpect(content().string(containsString("Andreas")))
				.andExpect(content().string(containsString("Sandra")));
	}

	@Test
	void testCreatePerson() throws Exception {
		PhoneBookEntry neu = new PhoneBookEntry(null, "Max", "Mustermann", "+49", "1700000000");
		String json = new ObjectMapper().writeValueAsString(neu);

		mockMvc.perform(post("/api/phonebook")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated());
	}
}
