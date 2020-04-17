package pl.coderslab.charity.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.repository.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService service;

    @Test
    void shouldReturnAllCategories() {
        //given
        Category a = new Category("A");
        a.setId(1L);
        Category b = new Category("B");
        b.setId(2L);
        List<Category> categories = List.of(a, b);
        given(categoryRepository.findAll()).willReturn(categories);

        //when
        Set<Category> all = service.getAll();

        //then
        then(categoryRepository).should().findAll();
        assertThat(all).hasSize(2).containsExactlyInAnyOrder(a, b);
    }

    @Test
    void ifCategoriesNotExistShouldReturnEmptySet() {
        //given
        given(categoryRepository.findAll()).willReturn(Collections.emptyList());

        //when
        Set<Category> all = service.getAll();

        //then
        then(categoryRepository).should().findAll();
        assertThat(all).isEmpty();
    }
}
