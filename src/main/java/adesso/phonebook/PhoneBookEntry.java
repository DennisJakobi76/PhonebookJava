package adesso.phonebook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBookEntry {
	private Long id;
	private String firstName;
	private String lastName;
	private String phonePrefix;
	private String phoneNumber;
}
