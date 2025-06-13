package adesso.phonebook.util;

import adesso.phonebook.PhoneBookEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PhoneBookEntryTestDataBuilder {

    private String firstName = "Max";
    private String lastName = "Mustermann";
    private String phonePrefix = "+49";
    private String phoneNumber = "123456789";

    public PhoneBookEntryTestDataBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PhoneBookEntryTestDataBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PhoneBookEntryTestDataBuilder withPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
        return this;
    }

    public PhoneBookEntryTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PhoneBookEntryDto build() {
        return new PhoneBookEntryDto(null, firstName, lastName, phonePrefix, phoneNumber);
    }

    public PhoneBookEntryDto save(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        PhoneBookEntryDto dto = build();
        MvcResult result = mockMvc.perform(post("/api/phonebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), PhoneBookEntryDto.class);
    }
}
