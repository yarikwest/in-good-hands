package pl.coderslab.charity.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(UserDonationsController.class)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
class UserDonationsControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    DonationService donationService;

    @Test
    void shouldReturnViewWithAllUserDonationsAsc() throws Exception {
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
        Page<Donation> donationPage = new PageImpl<>(List.of(donation1, donation2), PageRequest.of(0,5, Sort.Direction.ASC, "id"), 10);
        given(donationService.getAllByUserSorted(any(User.class), any(Pageable.class))).willReturn(donationPage);

        //when //then
        mockMvc.perform(get("/user/donations")
                    .param("page", "1")
                    .param("size", "5")
                    .param("sortBy", "id")
                    .param("asc", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("donations", donationPage))
                .andExpect(model().attribute("pageNumbers", List.of(1,2)))
                .andExpect(view().name("user/donations"));
    }

    @Test
    void shouldReturnViewWithAllUserDonationsDesc() throws Exception {
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
        Page<Donation> donationPage = new PageImpl<>(List.of(donation1, donation2), PageRequest.of(0,5, Sort.Direction.DESC, "id"), 10);
        given(donationService.getAllByUserSorted(any(User.class), any(Pageable.class))).willReturn(donationPage);

        //when //then
        mockMvc.perform(get("/user/donations")
                    .param("page", "1")
                    .param("size", "5")
                    .param("sortBy", "id")
                    .param("asc", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("donations", donationPage))
                .andExpect(model().attribute("pageNumbers", List.of(1,2)))
                .andExpect(view().name("user/donations"));
    }
}
