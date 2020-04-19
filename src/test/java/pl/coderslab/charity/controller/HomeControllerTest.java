package pl.coderslab.charity.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    DonationService donationService;

    @Mock
    InstitutionService institutionService;

    @InjectMocks
    HomeController controller;

    MockMvc mockMvc;

    Set<Institution> institutions;

    @BeforeEach
    void setUp() {
        Institution e1 = new Institution("name1", "desc1");
        e1.setId(1L);
        Institution e2 = new Institution("name2", "desc2");
        e2.setId(2L);
        institutions = Set.of(e1, e2);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        given(donationService.getCountAllDonations()).willReturn(5L);
        given(donationService.getSumOfAllPackage()).willReturn(5L);
        given(institutionService.getAll()).willReturn(institutions);
    }

    @Test
    void shouldReturnIndexViewWithModelForGetRequest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("institutions", equalTo(institutions)))
                .andExpect(model().attribute("countAllDonations", equalTo(5L)))
                .andExpect(model().attribute("sumOfAllPackage", equalTo(5L)))
                .andExpect(view().name("index"));
    }
}
