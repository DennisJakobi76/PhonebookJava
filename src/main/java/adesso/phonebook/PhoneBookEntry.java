package adesso.phonebook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phonebookentries",
uniqueConstraints = @UniqueConstraint(
        columnNames = {"first_name", "last_name", "phone_prefix", "phone_number"}))


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBookEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phonebookentries_seq")
	@SequenceGenerator(name = "phonebookentries_seq", sequenceName = "PHONEBOOKENTRIES_SEQ", allocationSize = 1, initialValue = 1)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "phone_prefix")
	private String phonePrefix;
	@Column(name = "phone_number")
	private String phoneNumber;
}
