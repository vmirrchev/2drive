package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.repository.BrandRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class BrandServiceTest {

    private final BrandRepository brandRepository = mock(BrandRepository.class);
    private final BrandService brandService = new BrandService(brandRepository);
    private final Brand brand = mock(Brand.class);
    private final Long brandId = 1L;

    @AfterEach
    public void clean() {
        reset(brandRepository);
    }

    @Test
    void getByIdShouldReturnBrand() {
        when(brandRepository.findById(brandId)).thenReturn(Optional.ofNullable(brand));

        final Brand result = brandService.getById(brandId);

        verify(brandRepository, times(1)).findById(brandId);
        assertEquals(brand, result);
    }

    @Test
    void getByIdShouldThrowWhenIdNull() {
        final Throwable thrown = assertThrows(RuntimeException.class, () -> brandService.getById(null));

        verify(brandRepository, times(0)).findById(any());
        assertEquals("Brand id cannot be null", thrown.getMessage());
    }

    @Test
    void getByIdShouldThrowWhenBrandNotFound() {
        final String exceptionMessage = MessageFormat.format("There is no brand for the given id ({0})", brandId);
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> brandService.getById(brandId));

        verify(brandRepository, times(1)).findById(brandId);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void getAllBrandsShouldReturnBrands() {
        final List<Brand> brands = List.of(brand, brand);
        when(brandRepository.findAll()).thenReturn(brands);

        final List<Brand> result = brandService.getAllBrands();

        verify(brandRepository, times(1)).findAll();
        assertEquals(brands, result);
    }

    @Test
    void getAllBrandsShouldReturnNoBrands() {
        when(brandRepository.findAll()).thenReturn(List.of());

        final List<Brand> result = brandService.getAllBrands();

        verify(brandRepository, times(1)).findAll();
        assertThat(result).isEmpty();
    }
}