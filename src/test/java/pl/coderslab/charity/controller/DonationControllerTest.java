package pl.coderslab.charity.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class DonationControllerTest {

    @Mock
    CategoryService categoryService;

    @Mock
    InstitutionService institutionService;

    @InjectMocks
    DonationController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnViewWithFormForAddingDonation() throws Exception {
        //given
        Category c1 = new Category("category1");
        c1.setId(1L);
        Category c2 = new Category("category2");
        c2.setId(2L);
        Set<Category> categories = Set.of(c1, c2);

        Institution e1 = new Institution("name1", "desc1");
        e1.setId(1L);
        Institution e2 = new Institution("name2", "desc2");
        e2.setId(2L);
        Set<Institution> institutions = Set.of(e1, e2);

        given(categoryService.getAll()).willReturn(categories);
        given(institutionService.getAll()).willReturn(institutions);

        //when //then
        mockMvc.perform(get("/donations"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("donation"))
                .andExpect(model().attribute("allCategories", equalTo(categories)))
                .andExpect(model().attribute("allInstitutions", equalTo(institutions)))
                .andExpect(view().name("form"));
    }
}
