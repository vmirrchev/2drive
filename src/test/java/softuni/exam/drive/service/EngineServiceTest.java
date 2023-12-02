package softuni.exam.drive.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.enums.FuelType;
import softuni.exam.drive.repository.EngineRepository;

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
class EngineServiceTest {

    private final EngineRepository engineRepository = mock(EngineRepository.class);
    private final BrandService brandService = mock(BrandService.class);
    private final EngineService engineService = new EngineService(engineRepository, brandService);
    private final EngineBindingModel engineBindingModel = mock(EngineBindingModel.class);
    private final Brand engineBrand = mock(Brand.class);
    private final Engine engine = mock(Engine.class);
    private final FuelType engineFuelType = FuelType.DIESEL;
    private final String engineName = "M47TUD20";
    private final Integer engineDisplacement = 1995;
    private final Integer engineHorsepower = 150;
    private final Long engineId = 1L;
    private final Long brandId = 1L;
    private final String exceptionMessage = "message";

    @BeforeEach
    public void setUp() {
        when(engineBindingModel.getFuelType()).thenReturn(engineFuelType);
        when(engineBindingModel.getName()).thenReturn(engineName);
        when(engineBindingModel.getDisplacement()).thenReturn(engineDisplacement);
        when(engineBindingModel.getHorsepower()).thenReturn(engineHorsepower);
    }

    @AfterEach
    public void clean() {
        reset(brandService, engineBindingModel);
    }

    @Test
    void createEngineShouldAddNewEngine() {
        final ArgumentCaptor<Engine> argumentCaptor = ArgumentCaptor.forClass(Engine.class);
        when(brandService.getById(any())).thenReturn(engineBrand);

        engineService.createEngine(engineBindingModel);

        verify(engineRepository, times(1)).existsByName(engineName);
        verify(engineRepository).save(argumentCaptor.capture());
        final Engine engine = argumentCaptor.getValue();
        assertEquals(engineBrand, engine.getBrand());
        assertEquals(engineFuelType, engine.getFuelType());
        assertEquals(engineName, engine.getName());
        assertEquals(engineDisplacement, engine.getDisplacement());
        assertEquals(engineHorsepower, engine.getHorsepower());
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
        when(brandService.getById(any())).thenReturn(engineBrand);
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
        when(brandService.getById(brandId)).thenReturn(engineBrand);
        when(engineRepository.findAllByBrand(engineBrand)).thenReturn(engines);

        assertEquals(engines, engineService.getAllEnginesByBrandId(brandId));
        verify(brandService, times(1)).getById(brandId);
        verify(engineRepository, times(1)).findAllByBrand(engineBrand);
    }

    @Test
    void getAllEnginesByBrandIdShouldThrowWhenBrandIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(brandId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.getAllEnginesByBrandId(brandId));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(brandService, times(1)).getById(brandId);
        verify(engineRepository, times(0)).findAllByBrand(engineBrand);
    }
}