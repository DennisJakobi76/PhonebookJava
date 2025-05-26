package adesso.phonebook;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneBookEntryMapper {

	@Mapping(target = "id", ignore = true)
	PhoneBookEntry dtoToEntity(PhoneBookEntryDto dto);

}
