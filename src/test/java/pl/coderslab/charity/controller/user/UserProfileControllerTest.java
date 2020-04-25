package pl.coderslab.charity.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.charity.controller.TestSecurityConfiguration;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.dto.UserMapper;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(UserProfileController.class)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
class UserProfileControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserMapper userMapper;

    @MockBean
    UserService userService;

    UserDto userDto;

    @BeforeEach
    void setUp() {
        //given
        User user = User.builder().email("email@test").build();
        given(userService.getUserByEmail(anyString())).willReturn(user);

        userDto = new UserDto(1L, "email@test", true);
        given(userMapper.userToUserDto(any(User.class))).willReturn(userDto);
    }

    @Test
    void shouldReturnViewForUpdatingEmailAndPassword() throws Exception {
        //when //then
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("updatePassword"))
                .andExpect(model().attribute("user", userDto))
                .andExpect(view().name("user/profile"));
    }

    @Test
    void shouldChangeUserEmail() throws Exception {
        //when //then
        mockMvc.perform(post("/user/profile/update-email")
                  .param("email", "emailChange@test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));
    }

    @Test
    void ifFormInvalidShouldReturnChangeEmailFormWithErrors() throws Exception {
        //when //then
        mockMvc.perform(post("/user/profile/update-email")
                    .param("email", "invalid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("updatePassword"))
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(view().name("user/profile"));
    }

    @Test
    void ifEmailAlreadyExistsShouldReturnEmailChangeFormWithErrors() throws Exception {
        //given
        given(userService.checkIfEmailAlreadyExists(anyString())).willReturn(true);

        //when //then
        mockMvc.perform(post("/user/profile/update-email")
                    .param("email", "existEmail@test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("updatePassword"))
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(view().name("user/profile"));
    }

    @Test
    void shouldChangeUserPassword() throws Exception {
        //given
        given(userService.checkIfValidOldPassword(any(), anyString())).willReturn(true);

        //when //then
        mockMvc.perform(post("/user/profile/update-password")
                    .param("oldPassword", "oldPass")
                    .param("password", "New@Password1")
                    .param("matchingPassword", "New@Password1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));
    }

    @Test
    void ifFormInvalidShouldReturnChangePasswordFormWithErrors() throws Exception {
        //given
        given(userService.checkIfValidOldPassword(any(), anyString())).willReturn(true);

        //when //then
        mockMvc.perform(post("/user/profile/update-password")
                    .param("oldPassword", "invalid")
                    .param("password", "short")
                    .param("matchingPassword", "short"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasErrors("updatePassword"))
                .andExpect(model().attributeHasFieldErrors("updatePassword", "password"))
                .andExpect(view().name("user/profile"));
    }

    @Test
    void ifOldPasswordInvalidShouldReturnChangePasswordFormWithErrors() throws Exception {
        //given
        given(userService.checkIfValidOldPassword(any(), anyString())).willReturn(false);

        //when //then
        mockMvc.perform(post("/user/profile/update-password")
                    .param("oldPassword", "invalid")
                    .param("password", "New@Password1")
                    .param("matchingPassword", "New@Password1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasErrors("updatePassword"))
                .andExpect(model().attributeHasFieldErrors("updatePassword", "oldPassword"))
                .andExpect(view().name("user/profile"));
    }
}
