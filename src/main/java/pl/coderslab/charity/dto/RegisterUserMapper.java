package pl.coderslab.charity.dto;

import org.mapstruct.Mapper;
import pl.coderslab.charity.model.User;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

    User registerUserDtoToUser(RegisterUserDto registerUserDto);
}
