package adesso.phonebook.integration;

import static org.hamcrest.Matchers.containsString;

import adesso.phonebook.PhoneBookEntryDto;
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
       void getAllEntries_shouldReturnAllItems() throws Exception {
              mockMvc.perform(get("/api/phonebook")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                            .andExpect(jsonPath("$[0].firstName").exists())
                            .andExpect(jsonPath("$[0].lastName").exists());
       }

       @Test
       void filterEntries_shouldReturnMatchingEntries() throws Exception {
              mockMvc.perform(get("/api/phonebook").param("userInput", "an")).andExpect(status().isOk())
                            .andExpect(jsonPath("$[0].firstName", containsString("An")));
       }

       @Test
       void getEntryById_existingId_shouldReturnEntry() throws Exception {
              mockMvc.perform(get("/api/phonebook/1")).andExpect(status().isOk())
                            .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.firstName").exists());
       }

       @Test
       void getEntryById_nonExistingId_shouldReturn404() throws Exception {
              mockMvc.perform(get("/api/phonebook/999")).andExpect(status().isNotFound());
       }

       @Test
       void createEntry_validData_shouldReturnCreated() throws Exception {
              PhoneBookEntryDto newEntry = new PhoneBookEntryDto(null, "Test", "User", "+49", "1234567890");

              String response = mockMvc.perform(post("/api/phonebook")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(newEntry)))
                      .andExpect(status().isCreated())
                      .andExpect(jsonPath("$.id").exists())
                      .andExpect(jsonPath("$.firstName").value("Test"))
                      .andReturn().getResponse().getContentAsString();

              PhoneBookEntryDto created = objectMapper.readValue(response, PhoneBookEntryDto.class);
       }

       @Test
       void updateEntry_existingId_shouldReturnOk() throws Exception {
              PhoneBookEntryDto updateEntry = new PhoneBookEntryDto();
              updateEntry.setFirstName("Updated");
              updateEntry.setLastName("User");
              updateEntry.setPhonePrefix("+49");
              updateEntry.setPhoneNumber("9876543210");

              mockMvc.perform(put("/api/phonebook/100").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateEntry))).andExpect(status().isOk());

              // Verify the update
              mockMvc.perform(get("/api/phonebook/100")).andExpect(status().isOk())
                            .andExpect(jsonPath("$.firstName").value("Updated"));
       }

       @Test
       void updateEntry_nonExistingId_shouldReturn404() throws Exception {
              PhoneBookEntryDto updateEntry = new PhoneBookEntryDto();
              updateEntry.setFirstName("Test");
              updateEntry.setLastName("User");
              updateEntry.setPhonePrefix("+49");
              updateEntry.setPhoneNumber("1234567890");

              mockMvc.perform(put("/api/phonebook/999").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateEntry))).andExpect(status().isNotFound());
       }

       @Test
       void deleteEntry_existingId_shouldReturnNoContent() throws Exception {
              PhoneBookEntryDto newEntry = new PhoneBookEntryDto(null, "ToDelete", "User", "+49", "1234567890");

              String response = mockMvc.perform(post("/api/phonebook")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(objectMapper.writeValueAsString(newEntry)))
                      .andReturn().getResponse().getContentAsString();

              PhoneBookEntryDto created = objectMapper.readValue(response, PhoneBookEntryDto.class);
              Long createdId = created.getId();

              mockMvc.perform(delete("/api/phonebook/" + createdId))
                      .andExpect(status().isNoContent());

              mockMvc.perform(get("/api/phonebook/" + createdId))
                      .andExpect(status().isNotFound());
       }

       @Test
       void deleteEntry_nonExistingId_shouldReturn404() throws Exception {
              mockMvc.perform(delete("/api/phonebook/1999")).andExpect(status().isNotFound());
       }

       @Test
       void searchByPrefix_shouldReturnMatchingEntries() throws Exception {
              mockMvc.perform(get("/api/phonebook").param("userInput", "+49")).andExpect(status().isOk())
                            .andExpect(jsonPath("$[0].phonePrefix").value("+49"));
       }
}
