package pl.coderslab.charity.dto;

import org.mapstruct.Mapper;
import pl.coderslab.charity.model.User;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    User adminDtoToUser(AdminDto adminDto);

    AdminDto userToAdminDto(User user);

    Set<User> adminDtosToUsers(Set<AdminDto> adminDtos);

    Set<AdminDto> usersToAdminDtos(Set<User> users);
}
