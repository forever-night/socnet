package socnet.mappers;

import fr.xebia.extras.selma.*;
import socnet.dto.PublicMessageDto;
import socnet.entities.PublicMessage;


@Mapper(withIoC = IoC.SPRING, withIgnoreFields = {"senderLogin"})
public interface PublicMessageMapper {
    @Maps(withIgnoreMissing = IgnoreMissing.DESTINATION)
    PublicMessageDto asPublicMessageDto(PublicMessage publicMessage);
    
    @Maps(withIgnoreMissing = IgnoreMissing.SOURCE)
    PublicMessage asPublicMessage(PublicMessageDto publicMessageDto);
}
