package adesso.phonebook;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneBookEntryService {

	@Autowired
	private PhoneBookEntryRepository phoneBookEntryRepository;

	public List<PhoneBookEntry> getAll() {
		return phoneBookEntryRepository.findAll();
	}

	public Optional<PhoneBookEntry> getById(Long id) {
		return phoneBookEntryRepository.findById(id);
	}

	public PhoneBookEntry add(PhoneBookEntry entry) {
		return phoneBookEntryRepository.save(entry);
	}

	public boolean update(Long id, PhoneBookEntry updated) {
		if (phoneBookEntryRepository.existsById(id)) {
			updated.setId(id);
			phoneBookEntryRepository.save(updated);
			return true;
		}
		return false;
	}

	public boolean delete(Long id) {
		if (phoneBookEntryRepository.existsById(id)) {
			phoneBookEntryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<PhoneBookEntry> filterByNameOrPrefix(String userInput) {
		return phoneBookEntryRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhonePrefixContainingIgnoreCase(
						userInput, userInput, userInput);
	}
}