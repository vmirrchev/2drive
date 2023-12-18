package softuni.exam.drive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.entity.Engine;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class EngineServiceTest extends BaseTest {

    private final EngineService engineService = new EngineService(engineRepository, brandService);

    @BeforeEach
    public void setUp() {
        when(engineBindingModel.getFuelType()).thenReturn(fuelType);
        when(engineBindingModel.getName()).thenReturn(engineName);
        when(engineBindingModel.getDisplacement()).thenReturn(displacement);
        when(engineBindingModel.getHorsepower()).thenReturn(horsepower);
    }

    @Test
    void createEngineShouldAddNewEngine() {
        final ArgumentCaptor<Engine> argumentCaptor = ArgumentCaptor.forClass(Engine.class);
        when(brandService.getById(any())).thenReturn(brand);

        engineService.createEngine(engineBindingModel);

        verify(engineRepository, times(1)).existsByName(engineName);
        verify(engineRepository).save(argumentCaptor.capture());
        final Engine engine = argumentCaptor.getValue();
        assertEquals(brand, engine.getBrand());
        assertEquals(fuelType, engine.getFuelType());
        assertEquals(engineName, engine.getName());
        assertEquals(displacement, engine.getDisplacement());
        assertEquals(horsepower, engine.getHorsepower());
    }

    @Test
    void createEngineShouldThrowWhenInvalidBrandId() {
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(any());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.createEngine(engineBindingModel));

        verify(engineBindingModel, times(1)).getName();
        verify(engineRepository, times(1)).existsByName(engineName);
        verify(engineBindingModel, times(0)).getFuelType();
        verify(engineBindingModel, times(0)).getDisplacement();
        verify(engineBindingModel, times(0)).getHorsepower();
        verify(engineRepository, times(0)).save(any());
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void createEngineShouldThrowWhenNameUsed() {
        final String exceptionMessage = MessageFormat.format("There is already an engine with the given name ({0})", engineName);
        when(brandService.getById(any())).thenReturn(brand);
        when(engineRepository.existsByName(engineName)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.createEngine(engineBindingModel));

        verify(engineBindingModel, times(1)).getName();
        verify(engineRepository, times(1)).existsByName(engineName);
        verify(engineBindingModel, times(0)).getFuelType();
        verify(engineBindingModel, times(0)).getDisplacement();
        verify(engineBindingModel, times(0)).getHorsepower();
        verify(engineRepository, times(0)).save(any());
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void getEngineByIdShouldReturnEngine() {
        final Engine engine = new Engine();
        when(engineRepository.findById(engineId)).thenReturn(Optional.of(engine));

        assertEquals(engine, engineService.getEngineById(engineId));
        verify(engineRepository, times(1)).findById(engineId);
    }

    @Test
    void getEngineByIdShouldThrowWhenIdNull() {
        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.getEngineById(null));

        assertEquals(thrown.getMessage(), "Engine id cannot be null");
        verify(engineRepository, times(0)).findById(any());
    }

    @Test
    void getEngineByIdShouldThrowWhenIdInvalid() {
        final String exceptionMessage = MessageFormat.format("There is no engine for the given id ({0})", engineId);
        when(engineRepository.findById(engineId)).thenReturn(Optional.empty());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.getEngineById(engineId));

        assertEquals(thrown.getMessage(), exceptionMessage);
        verify(engineRepository, times(1)).findById(engineId);
    }

    @Test
    void getAllEnginesByBrandIdShouldReturnList() {
        final List<Engine> engines = List.of(engine);
        when(brandService.getById(brandId)).thenReturn(brand);
        when(engineRepository.findAllByBrand(brand)).thenReturn(engines);

        assertEquals(engines, engineService.getAllEnginesByBrandId(brandId));
        verify(brandService, times(1)).getById(brandId);
        verify(engineRepository, times(1)).findAllByBrand(brand);
    }

    @Test
    void getAllEnginesByBrandIdShouldThrowWhenBrandIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(brandId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.getAllEnginesByBrandId(brandId));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(brandService, times(1)).getById(brandId);
        verify(engineRepository, times(0)).findAllByBrand(brand);
    }
}