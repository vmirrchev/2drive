package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.repository.BrandRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class InitServiceTest {

    private final BrandRepository brandRepository = mock(BrandRepository.class);
    private final InitService initService = new InitService(brandRepository);

    @AfterEach
    public void clean() {
        reset(brandRepository);
    }

    @Test
    void runShouldCreateBrands() {
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        when(brandRepository.findAll()).thenReturn(List.of());

        initService.run();

        verify(brandRepository).saveAll(argumentCaptor.capture());
        final List<Brand> brands = argumentCaptor.getValue();
        assertEquals("Bmw", brands.get(0).getName());
        assertEquals("Mercedes", brands.get(1).getName());
        assertEquals("Audi", brands.get(2).getName());
    }

    @Test
    void runShouldNotCreateBrands() {
        when(brandRepository.findAll()).thenReturn(List.of(mock(Brand.class)));

        initService.run();

        verify(brandRepository, times(0)).saveAll(any());
    }
}