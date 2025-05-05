package adesso.phonebook;

public class PhoneBookEntry {
	private Long id;
	private String vorname;
	private String nachname;
	private String telefonVorwahl;
	private String telefonnummer;

	public PhoneBookEntry() {
	}

	public PhoneBookEntry(Long id, String vorname, String nachname, String telefonVorwahl, String telefonnummer) {
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.telefonVorwahl = telefonVorwahl;
		this.telefonnummer = telefonnummer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getTelefonVorwahl() {
		return telefonVorwahl;
	}

	public void setTelefonVorwahl(String telefonVorwahl) {
		this.telefonVorwahl = telefonVorwahl;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}
}
