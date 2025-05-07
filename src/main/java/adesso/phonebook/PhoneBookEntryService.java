package adesso.phonebook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PhoneBookEntryService {
	private final List<PhoneBookEntry> phoneBookEntries = new ArrayList<>();

	public PhoneBookEntryService() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<PhoneBookEntry>> typeRef = new TypeReference<>() {
			};
			InputStream is = getClass().getResourceAsStream("/data.json");
			phoneBookEntries.addAll(mapper.readValue(is, typeRef));
		} catch (IOException e) {
			throw new RuntimeException("Konnte data.json nicht laden", e);
		}
	}

	public List<PhoneBookEntry> getAll() {
		return phoneBookEntries;
	}

	public Optional<PhoneBookEntry> getById(Long id) {
		return phoneBookEntries.stream().filter(entry -> entry.getId().equals(id)).findFirst();
	}

	public void add(PhoneBookEntry entry) {
		entry.setId(getNextId());
		phoneBookEntries.add(entry);
	}

	public boolean update(Long id, PhoneBookEntry updated) {
		return getById(id).map(entry -> {
			entry.setFirstName(updated.getFirstName());
			entry.setLastName(updated.getLastName());
			entry.setPhonePrefix(updated.getPhonePrefix());
			entry.setPhoneNumber(updated.getPhoneNumber());
			return true;
		}).orElse(false);
	}

	public boolean delete(Long id) {
		return phoneBookEntries.removeIf(entry -> entry.getId().equals(id));
	}

	private Long getNextId() {
		return phoneBookEntries.stream()
				.mapToLong(PhoneBookEntry::getId)
				.max()
				.orElse(0L) + 1;
	}

	public List<PhoneBookEntry> filterByNameOrPrefix(String name) {
		String lowerName = name.toLowerCase();
		return phoneBookEntries.stream()
				.filter(entry -> entry.getFirstName().toLowerCase().contains(lowerName) ||
						entry.getLastName().toLowerCase().contains(lowerName) ||
						entry.getPhonePrefix().toLowerCase().contains(lowerName))
				.collect(Collectors.toList());
	}
}
