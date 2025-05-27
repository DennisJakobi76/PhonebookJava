package adesso.phonebook.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import adesso.phonebook.PhoneBookEntry;
import adesso.phonebook.PhoneBookEntryController;
import adesso.phonebook.PhoneBookEntryService;

@WebMvcTest(PhoneBookEntryController.class)
class PhoneBookEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneBookEntryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PhoneBookEntry testEntry;
    private List<PhoneBookEntry> testEntries;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        testEntry = new PhoneBookEntry(1L, "John", "Doe", "+49", "123456789");
        testEntries = List.of(testEntry, new PhoneBookEntry(2L, "Jane", "Smith", "+49", "987654321"),
                new PhoneBookEntry(3L, "Anna", "Miller", "+43", "123123123"));
    }

    @Test
    void getAllEntries_shouldReturnAllEntries() throws Exception {
        given(service.getAll()).willReturn(testEntries);

        mockMvc.perform(get("/api/phonebook")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3)).andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[2].firstName").value("Anna"));
    }

    @Test
    void filterEntries_shouldReturnMatchingEntries() throws Exception {
        given(service.filterByNameOrPrefix("an")).willReturn(List.of(testEntries.get(2)) // Only Anna matches
        );

        mockMvc.perform(get("/api/phonebook").param("userInput", "an")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Anna"));
    }

    @Test
    void getEntryById_existingEntry_shouldReturnEntry() throws Exception {
        given(service.getById(1L)).willReturn(Optional.of(testEntry));

        mockMvc.perform(get("/api/phonebook/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John")).andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getEntryById_nonExistingEntry_shouldReturn404() throws Exception {
        given(service.getById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/phonebook/99")).andExpect(status().isNotFound());
    }

    @Test
    void createEntry_validEntry_shouldReturnCreatedEntry() throws Exception {
        PhoneBookEntry newEntry = new PhoneBookEntry(null, "Max", "Mustermann", "+49", "555666777");
        PhoneBookEntry savedEntry = new PhoneBookEntry(4L, "Max", "Mustermann", "+49", "555666777");

        given(service.add(any(PhoneBookEntry.class))).willReturn(savedEntry);

        mockMvc.perform(post("/api/phonebook").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEntry))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.firstName").value("Max"))
                .andExpect(jsonPath("$.lastName").value("Mustermann"));
    }

    @Test
    void updateEntry_existingEntry_shouldReturnOk() throws Exception {
        PhoneBookEntry updateEntry = new PhoneBookEntry(null, "Updated", "Person", "+49", "999888777");

        given(service.update(eq(1L), any(PhoneBookEntry.class))).willReturn(true);

        mockMvc.perform(put("/api/phonebook/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEntry))).andExpect(status().isOk());
    }

    @Test
    void updateEntry_nonExistingEntry_shouldReturn404() throws Exception {
        PhoneBookEntry updateEntry = new PhoneBookEntry(null, "Updated", "Person", "+49", "999888777");

        given(service.update(eq(99L), any(PhoneBookEntry.class))).willReturn(false);

        mockMvc.perform(put("/api/phonebook/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEntry))).andExpect(status().isNotFound());
    }

    @Test
    void deleteEntry_existingEntry_shouldReturnNoContent() throws Exception {
        given(service.delete(1L)).willReturn(true);

        mockMvc.perform(delete("/api/phonebook/1")).andExpect(status().isNoContent());
    }

    @Test
    void deleteEntry_nonExistingEntry_shouldReturn404() throws Exception {
        given(service.delete(99L)).willReturn(false);

        mockMvc.perform(delete("/api/phonebook/99")).andExpect(status().isNotFound());
    }
}
