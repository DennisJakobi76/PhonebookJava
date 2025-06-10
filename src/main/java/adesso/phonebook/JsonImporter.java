package adesso.phonebook;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonImporter implements CommandLineRunner {

	private final PhoneBookEntryService phoneBookEntryService;
	private final PhoneBookEntryMapper mapper;

	public JsonImporter(PhoneBookEntryService service, PhoneBookEntryMapper mapper) {
		this.phoneBookEntryService = service;
		this.mapper = mapper;
	}

@Override
public void run(String... args) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    InputStream is = getClass().getResourceAsStream("/data.json");

    List<PhoneBookEntryDto> dtoList = objectMapper.readValue(is, new TypeReference<>() {});
    List<PhoneBookEntry> entries = dtoList.stream()
        .map(this.mapper::dtoToEntity)
        .toList();
    phoneBookEntryService.importIfNotExists(entries);
}
}
