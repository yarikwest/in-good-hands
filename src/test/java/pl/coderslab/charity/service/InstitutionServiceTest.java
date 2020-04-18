package pl.coderslab.charity.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.charity.exceptions.InstitutionIsNullException;
import pl.coderslab.charity.exceptions.InstitutionNotFoundException;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    InstitutionRepository repository;

    @InjectMocks
    InstitutionService service;

    @Test
    void shouldReturnAllInstitutions() {
        //given
        Institution institution1 = new Institution("name1", "desc1");
        institution1.setId(1L);
        Institution institution2 = new Institution("name2", "desc2");
        institution2.setId(2L);
        given(repository.findAll()).willReturn(asList(institution1, institution2));
        //when
        Set<Institution> all = service.getAll();

        //then
        then(repository).should().findAll();
        assertThat(all).hasSize(2).containsExactlyInAnyOrder(institution1, institution2);
    }

    @Test
    void ifInstitutionsNotExistShouldReturnEmptySet() {
        //given
        given(repository.findAll()).willReturn(emptyList());

        //when
        Set<Institution> all = service.getAll();

        //then
        then(repository).should().findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void shouldFindAndReturnInstitutionById() {
        //given
        Institution institution = new Institution();
        given(repository.findById(anyLong())).willReturn(Optional.of(institution));

        //when
        Institution byId = service.getById(1L);

        //then
        then(repository).should().findById(anyLong());
        assertThat(byId).isNotNull();
    }

    @Test
    void ifInstitutionNotExistShouldThrowException() {
        //given
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        //when //then
        assertThrows(InstitutionNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void shouldCreateInstitutionAndReturnSaved() {
        //given
        Institution institution = new Institution();
        institution.setId(1L);
        given(repository.save(any(Institution.class))).willReturn(institution);

        //when
        Institution savedInstitution = service.create(new Institution());

        //then
        then(repository).should().save(any(Institution.class));
        assertThat(savedInstitution).isNotNull().hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void ifInstitutionForCreatingIsNullShouldThrowException() {
        //when //then
        assertThrows(InstitutionIsNullException.class, () -> service.create(null));
    }

    @Test
    void shouldDeleteInstitutionById() {
        //give
        given(repository.existsById(anyLong())).willReturn(true);

        //when
        service.delete(1L);

        //then
        then(repository).should().deleteById(anyLong());
    }

    @Test
    void ifInstitutionNotExistForDeletingThrowException() {
        //give
        given(repository.existsById(anyLong())).willReturn(false);

        //when //then
        assertThrows(InstitutionNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void shouldReturnNumberOfAllInstitutions() {
        //given
        given(repository.count()).willReturn(2L);

        //when
        long countAll = service.getCountAll();

        //then
        assertEquals(2, countAll);
    }

    @Test
    void shouldUpdateInstitution() {
        //given
        Institution institution = new Institution();
        institution.setId(1L);
        given(repository.save(any(Institution.class))).willReturn(institution);
        given(repository.existsById(anyLong())).willReturn(true);

        //when
        Institution update = service.update(institution.getId(), institution);

        //then
        then(repository).should().save(any(Institution.class));
        assertEquals(1L, institution.getId());
    }

    @Test
    void ifInstitutionNotExistForUpdatingThrowException() {
        //give
        Institution institution = new Institution();
        institution.setId(1L);
        given(repository.existsById(anyLong())).willReturn(false);

        //when //then
        assertThrows(InstitutionNotFoundException.class, () -> service.update(institution.getId(), institution));
    }
}
