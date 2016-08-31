package socnet.mappers;

import fr.xebia.extras.selma.*;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;


@Mapper(withIoC = IoC.SPRING, withIgnoreMissing = IgnoreMissing.ALL,
        withCustomFields = {@Field({"city", "currentCity"})})
public interface ProfileMapper {
    @Maps
    ProfileDto asProfileDto(Profile source);
    
    @Maps
    Profile asProfile(ProfileDto source);
}
