package adesso.phonebook;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneBookEntryRepository extends JpaRepository<PhoneBookEntry, Long> {
    // Methode für die Suche nach Vor- oder Nachname (case-insensitive)
    List<PhoneBookEntry> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Methode für die Suche nach Vorwahl
    List<PhoneBookEntry> findByPhonePrefixContainingIgnoreCase(String prefix);
    
    // Methode für die Suche nach Telefonnummer
    List<PhoneBookEntry> findByPhoneNumberContainingIgnoreCase(String number);
    
    // Optional: Kombinierte Suche über alle Felder
    List<PhoneBookEntry> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhonePrefixContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
        String firstName, String lastName, String prefix, String number);
}
