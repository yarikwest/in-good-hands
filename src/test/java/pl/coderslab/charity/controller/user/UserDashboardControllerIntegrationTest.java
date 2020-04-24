package pl.coderslab.charity.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.charity.controller.TestSecurityConfiguration;
import pl.coderslab.charity.model.*;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockUser
@WebMvcTest(UserDashboardController.class)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
class UserDashboardControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    DonationService donationService;

    @Test
    void shouldReturnUserPanelViewWithData() throws Exception {
        //given
        User user = User.builder().email(SecurityContextHolder.getContext().getAuthentication().getName()).build();
        given(userService.getUserByEmail(anyString())).willReturn(user);
        Donation donation1 = Donation.builder()
                .zipCode("10-100")
                .street("Street 1/B")
                .status(DonationStatus.MISSED)
                .quantity(1)
                .pickUpTime(LocalTime.now())
                .pickUpDate(LocalDate.now())
                .pickUpComment("none")
                .phone("123456789")
                .city("London")
                .institution(Institution.builder().name("inst1").build())
                .categories(Set.of(Category.builder().name("cat1").build()))
                .user(user)
                .build();
        donation1.setId(1L);
        Donation donation2 = Donation.builder()
                .zipCode("10-100")
                .street("Street 1/B")
                .status(DonationStatus.RECEIVED)
                .quantity(1)
                .pickUpTime(LocalTime.now())
                .pickUpDate(LocalDate.now())
                .pickUpComment("none")
                .phone("123456789")
                .city("London")
                .institution(Institution.builder().name("inst2").build())
                .categories(Set.of(Category.builder().name("cat2").build()))
                .user(user)
                .build();
        donation2.setId(2L);
        Set<Donation> donations = Set.of(donation1, donation2);
        given(donationService.getCountAllDonationsByUser(any(User.class))).willReturn(2L);
        given(donationService.getSumOfAllPackageByUser(any(User.class))).willReturn(2L);
        given(donationService.getCountAllSupportedInstitutionsByUser(any(User.class))).willReturn(2L);
        given(donationService.getAllByUser(any(User.class))).willReturn(donations);

        //when //then
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("<h5 id=\"donationsCount\" class=\"mb-0\">2</h5>"),
                        containsString("<h5 id=\"quantityCount\" class=\"mb-0\">2</h5>"),
                        containsString("<h5 id=\"institutionsCount\" class=\"mb-0\">2</h5>")
                )))
                .andExpect(model().attribute("donationsCount", equalTo(2L)))
                .andExpect(model().attribute("quantityCount", equalTo(2L)))
                .andExpect(model().attribute("institutionsCount", equalTo(2L)))
                .andExpect(model().attribute("donations", donations))
                .andExpect(view().name("user/panel"));
    }
}
