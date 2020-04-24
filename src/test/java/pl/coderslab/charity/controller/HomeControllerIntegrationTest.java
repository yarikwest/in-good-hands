package pl.coderslab.charity.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.Set;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
class HomeControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DonationService donationService;

    @MockBean
    InstitutionService institutionService;

    Set<Institution> institutions;

    @BeforeEach
    void setUp() {
        Institution e1 = new Institution("name1", "desc1");
        e1.setId(1L);
        Institution e2 = new Institution("name2", "desc2");
        e2.setId(2L);
        institutions = Set.of(e1, e2);

        given(donationService.getCountAllDonations()).willReturn(5L);
        given(donationService.getSumOfAllPackage()).willReturn(5L);
        given(institutionService.getAll()).willReturn(institutions);
    }

    @Test
    void indexPageIsRenderedAsHtmlWithInstitutions() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("Foundation name1"),
                        containsString("Goal and mission: desc1"),
                        containsString("Foundation name2"),
                        containsString("Goal and mission: desc2")
                )));
    }
}
