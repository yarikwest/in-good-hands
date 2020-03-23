package pl.coderslab.charity.dto;

import org.mapstruct.Mapper;
import pl.coderslab.charity.model.User;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    Set<User> userDtosToUsers(Set<UserDto> userDtos);

    Set<UserDto> usersToUserDtos(Set<User> users);

}
