package softuni.exam.drive.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.enums.FuelType;
import softuni.exam.drive.repository.EngineRepository;

import java.text.MessageFormat;

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
    private final FuelType engineFuelType = FuelType.DIESEL;
    private final String engineName = "M47TUD20";
    private final Integer engineDisplacement = 1995;
    private final Integer engineHorsepower = 150;

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
        assertEquals(engineBrand, engine.getManufacturer());
        assertEquals(engineFuelType, engine.getFuelType());
        assertEquals(engineName, engine.getName());
        assertEquals(engineDisplacement, engine.getDisplacement());
        assertEquals(engineHorsepower, engine.getHorsepower());
    }

    @Test
    void createEngineShouldThrowWhenInvalidBrandId() {
        final String exceptionMessage = "message";
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(any());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> engineService.createEngine(engineBindingModel));

        verify(engineBindingModel, times(0)).getName();
        verify(engineRepository, times(0)).existsByName(engineName);
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
}