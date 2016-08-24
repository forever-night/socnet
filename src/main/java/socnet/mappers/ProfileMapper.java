package socnet.mappers;

import fr.xebia.extras.selma.*;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;


@Mapper(withCustomFields = {
    @Field({"city", "currentCity"})}, withIoC = IoC.SPRING)
public interface ProfileMapper {
    @Maps(withIgnoreMissing = IgnoreMissing.DESTINATION)
    ProfileDto asProfileDto(Profile source);
    
    @Maps(withIgnoreMissing = IgnoreMissing.SOURCE)
    Profile asProfile(ProfileDto source);
}
