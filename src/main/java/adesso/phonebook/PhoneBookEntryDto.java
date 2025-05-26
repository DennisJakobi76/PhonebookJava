package adesso.phonebook;

import lombok.Data;

@Data
public class PhoneBookEntryDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String phonePrefix;
	private String phoneNumber;
}
