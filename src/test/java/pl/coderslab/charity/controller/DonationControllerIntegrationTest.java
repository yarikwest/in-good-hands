package pl.coderslab.charity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(DonationController.class)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
class DonationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    DonationService donationService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    InstitutionService institutionService;

    @Test
    void shouldSaveNewDonationAndReturnFormConfirmationView() throws Exception {
        //given
        User user = new User();
        user.setEmail("user");
        given(userService.getUserByEmail(anyString())).willReturn(user);

        //when //then
        mockMvc.perform(post("/donations")
                    .param("city", "London")
                    .param("phone", "123456789")
                    .param("pickUpComment", "comment")
                    .param("pickUpDate", "2020-01-01")
                    .param("pickUpTime", "10:00")
                    .param("quantity", "1")
                    .param("street", "Street 1/5B")
                    .param("zipCode", "10-100")
                    .param("institution.id", "1")
                    .param("categories", "cat1, cat2"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("donation"))
                .andExpect(view().name("form-confirmation"));
    }

    @Test
    void ifFormInvalidShouldReturnFormViewWithErrors() throws Exception {
        //when //then
        mockMvc.perform(post("/donations")
                    .param("phone", "123456789")
                    .param("pickUpDate", "2020-01-01")
                    .param("pickUpTime", "10:00")
                    .param("quantity", "1")
                    .param("street", "Street 1/5B")
                    .param("zipCode", "10-100")
                    .param("categories", "cat1, cat2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("donation"))
                .andExpect(model().attributeHasFieldErrors("donation", "city"))
                .andExpect(model().attributeHasFieldErrors("donation", "institution"))
                .andExpect(view().name("form"));
    }
}
