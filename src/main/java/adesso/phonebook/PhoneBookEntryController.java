package adesso.phonebook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phonebook")
@CrossOrigin(origins = "*")
public class PhoneBookEntryController {

	@Autowired
	private PhoneBookEntryService phoneBookEntryService;

	@GetMapping
	public List<PhoneBookEntry> getAll(@RequestParam(required = false) String userInput) {
		if (userInput == null || userInput.isBlank()) {
			return phoneBookEntryService.getAll();
		} else {
			return phoneBookEntryService.filterByNameOrPrefix(userInput);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<PhoneBookEntry> getById(@PathVariable Long id) {
		return phoneBookEntryService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<PhoneBookEntry> create(@RequestBody PhoneBookEntry phoneBookEntry) {
		PhoneBookEntry saved = phoneBookEntryService.add(phoneBookEntry);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody PhoneBookEntry phoneBookEntry) {
		return phoneBookEntryService.update(id, phoneBookEntry) ? ResponseEntity.ok().build()
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return phoneBookEntryService.delete(id) ? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
