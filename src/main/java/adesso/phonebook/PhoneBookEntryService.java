package adesso.phonebook;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneBookEntryService {

	@Autowired
	private PhoneBookEntryRepository phoneBookEntryRepository;
	@Autowired
	private PhoneBookEntryMapper mapper;

	private static final Logger log = Logger.getLogger(PhoneBookEntryService.class.getName());

	public List<PhoneBookEntryDto> getAll(String userInput) {
		List<PhoneBookEntry> entries = (userInput == null || userInput.isBlank())
				? phoneBookEntryRepository.findAll()
				: phoneBookEntryRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhonePrefixContainingIgnoreCase(
						userInput, userInput, userInput);
		return entries.stream().map(mapper::entityToDto).toList();
	}

	public Optional<PhoneBookEntryDto> getById(Long id) {
		return phoneBookEntryRepository.findById(id).map(mapper::entityToDto);
	}

	public PhoneBookEntryDto add(PhoneBookEntryDto dto) {
		PhoneBookEntry entry = mapper.dtoToEntity(dto);
		PhoneBookEntry saved = phoneBookEntryRepository.save(entry);
		return mapper.entityToDto(saved);
	}

	public boolean update(Long id, PhoneBookEntryDto dto) {
		if (phoneBookEntryRepository.existsById(id)) {
			PhoneBookEntry entry = mapper.dtoToEntity(dto);
			entry.setId(id);
			phoneBookEntryRepository.save(entry);
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

	public List<PhoneBookEntryDto> filterByNameOrPrefix(String userInput) {
		return phoneBookEntryRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhonePrefixContainingIgnoreCase(
						userInput, userInput, userInput)
				.stream()
				.map(mapper::entityToDto)
				.toList();
	}

	public void importIfNotExists(List<PhoneBookEntry> entries) {
		for (PhoneBookEntry entry : entries) {
			boolean exists = phoneBookEntryRepository.existsByFirstNameAndLastNameAndPhonePrefixAndPhoneNumber(
					entry.getFirstName(),
					entry.getLastName(),
					entry.getPhonePrefix(),
					entry.getPhoneNumber()
			);
			if (!exists) {
				phoneBookEntryRepository.save(entry);
			} else {
				log.info("Eintrag existiert bereits: " + entry);
			}
		}
	}
}