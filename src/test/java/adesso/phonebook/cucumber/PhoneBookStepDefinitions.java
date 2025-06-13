package adesso.phonebook.cucumber;

import adesso.phonebook.PhoneBookEntryDto;
import adesso.phonebook.util.PhoneBookEntryTestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PhoneBookStepDefinitions {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private MvcResult mvcResult;
    private Long createdId;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @When("I request all phone book entries")
    public void request_all_entries() throws Exception {
        mvcResult = mockMvc.perform(get("/api/phonebook")).andReturn();
    }

    @Then("the response should contain a list of entries")
    public void response_should_be_list() throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Given("a phone book entry with ID {long} exists")
    public void entry_with_id_exists(Long id) throws Exception {
        // Hier nur als Platzhalter, optional könnte man hier Daten anlegen
        // Wir verlassen uns aktuell darauf, dass ID 1 und 100 schon existieren
    }

    @When("I request the phone book entry with ID {long}")
    public void request_entry_by_id(Long id) throws Exception {
        mvcResult = mockMvc.perform(get("/api/phonebook/" + id)).andReturn();
    }

    @Then("the response should contain the entry with ID {long}")
    public void response_should_contain_entry(Long id) throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));
    }

    @Then("the response status should be 404 Not Found")
    public void response_should_be_404() throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @When("I create a phone book entry with first name {string}, last name {string}, phone prefix {string} and phone number {string}")
    public void create_entry(String firstName, String lastName, String prefix, String number) throws Exception {
        PhoneBookEntryDto dto = new PhoneBookEntryTestDataBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPhonePrefix(prefix)
                .withPhoneNumber(number)
                .build();

        mvcResult = mockMvc.perform(post("/api/phonebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
    }

    @Then("the response should contain the created entry with first name {string}")
    public void response_should_contain_created_entry(String firstName) throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(firstName)));
    }

    @When("I update the phone book entry with ID {long} to have first name {string}, last name {string}, phone prefix {string} and phone number {string}")
    public void update_entry(Long id, String firstName, String lastName, String prefix, String number) throws Exception {
        PhoneBookEntryDto dto = new PhoneBookEntryTestDataBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPhonePrefix(prefix)
                .withPhoneNumber(number)
                .build();

        mvcResult = mockMvc.perform(put("/api/phonebook/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
    }

    @Then("the phone book entry with ID {long} should have first name {string}")
    public void verify_updated_entry(Long id, String firstName) throws Exception {
        mockMvc.perform(get("/api/phonebook/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)));
    }

    @Given("a phone book entry with first name {string}, last name {string}, phone prefix {string} and phone number {string} exists")
    public void create_entry_for_deletion(String firstName, String lastName, String prefix, String number) throws Exception {
        PhoneBookEntryDto created = new PhoneBookEntryTestDataBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPhonePrefix(prefix)
                .withPhoneNumber(number)
                .save(mockMvc, objectMapper);

        createdId = created.getId();
    }

    @When("I delete the phone book entry")
    public void delete_created_entry() throws Exception {
        mvcResult = mockMvc.perform(delete("/api/phonebook/" + createdId)).andReturn();
    }

    @Then("the phone book entry should no longer exist")
    public void verify_entry_deleted() throws Exception {
        mockMvc.perform(get("/api/phonebook/" + createdId))
                .andExpect(status().isNotFound());
    }

    @When("I delete the phone book entry with ID {long}")
    public void delete_entry_by_id(Long id) throws Exception {
        mvcResult = mockMvc.perform(delete("/api/phonebook/" + id)).andReturn();
    }

    @When("I search phone book entries with user input {string}")
    public void search_entries(String userInput) throws Exception {
        mvcResult = mockMvc.perform(get("/api/phonebook").param("userInput", userInput)).andReturn();
    }

    @Then("the response should contain entries where first name contains {string}")
    public void verify_search_firstname(String text) throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", containsString(text)));
    }

    @Then("the response should contain entries with phone prefix {string}")
    public void verify_search_prefix(String prefix) throws Exception {
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phonePrefix", is(prefix)));
    }
}
