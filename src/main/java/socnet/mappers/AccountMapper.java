package socnet.mappers;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import socnet.dto.AccountDto;
import socnet.entities.Account;


@Mapper(withIoC = IoC.SPRING, withIgnoreFields = {"oldPassword"})
public interface AccountMapper {
    @Maps(withIgnoreMissing = IgnoreMissing.DESTINATION)
    AccountDto asAccountDto(Account source);
    
    @Maps(withIgnoreMissing = IgnoreMissing.SOURCE)
    Account asAccount(AccountDto source);
}
