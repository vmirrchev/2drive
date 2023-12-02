package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;
import softuni.exam.drive.repository.ModelRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * @author Vasil Mirchev
 */
class ModelServiceTest {

    private final ModelRepository modelRepository = mock(ModelRepository.class);
    private final BrandService brandService = mock(BrandService.class);
    private final EngineService engineService = mock(EngineService.class);
    private final ModelService modelService = new ModelService(modelRepository, brandService, engineService);
    private final ModelBindingModel modelBindingModel = mock(ModelBindingModel.class);
    private final Brand modelBrand = mock(Brand.class);
    private final String modelName = "3 Series";
    private final Long brandId = 1L;
    private final Long firstEngineId = 1L;
    private final Long secondEngineId = 2L;
    private final List<Long> engineIds = List.of(firstEngineId, secondEngineId);
    private final List<BodyType> bodyTypes = List.of(BodyType.SEDAN, BodyType.COUPE, BodyType.CONVERTIBLE, BodyType.WAGON);
    private final List<DriveType> driveTypes = List.of(DriveType.RWD, DriveType.AWD);
    private final List<TransmissionType> transmissionTypes = List.of(TransmissionType.MANUAL, TransmissionType.AUTOMATIC);
    private final Engine firstEngine = mock(Engine.class);
    private final Engine secondEngine = mock(Engine.class);
    private final Set<Engine> engines = Set.of(firstEngine, secondEngine);
    private final int startYear = 2004;
    private final int endYear = 2010;

    @BeforeEach
    public void setUp() {
        when(modelBindingModel.getName()).thenReturn(modelName);
        when(modelBindingModel.getBrandId()).thenReturn(brandId);
        when(modelBindingModel.getEngineIds()).thenReturn(engineIds);
        when(modelBindingModel.getBodyTypes()).thenReturn(bodyTypes);
        when(modelBindingModel.getDriveTypes()).thenReturn(driveTypes);
        when(modelBindingModel.getTransmissionTypes()).thenReturn(transmissionTypes);
        when(modelBindingModel.getStartYear()).thenReturn(startYear);
        when(modelBindingModel.getEndYear()).thenReturn(endYear);
    }

    @AfterEach
    public void clean() {
        reset(modelRepository, brandService, engineService);
    }

    @Test
    void createModelShouldAddNewModel() {
        final ArgumentCaptor<Model> argumentCaptor = ArgumentCaptor.forClass(Model.class);
        when(brandService.getById(any())).thenReturn(modelBrand);
        when(engineService.getEngineById(firstEngineId)).thenReturn(firstEngine);
        when(engineService.getEngineById(secondEngineId)).thenReturn(secondEngine);

        modelService.createModel(modelBindingModel);

        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(1)).getById(brandId);
        verify(engineService, times(1)).getEngineById(firstEngineId);
        verify(engineService, times(1)).getEngineById(secondEngineId);
        verify(modelRepository).save(argumentCaptor.capture());
        final Model model = argumentCaptor.getValue();
        assertEquals(modelName, model.getName());
        assertEquals(startYear, model.getStartYear());
        assertEquals(endYear, model.getEndYear());
        assertEquals(bodyTypes, model.getBodyTypes());
        assertEquals(driveTypes, model.getDriveTypes());
        assertEquals(transmissionTypes, model.getTransmissions());
        assertEquals(modelBrand, model.getBrand());
        assertEquals(engines, model.getEngines());
    }

    @Test
    void createModelShouldThrowWhenNameUsed() {
        final String exceptionMessage = MessageFormat.format("There is already a model with the given name ({0})", modelName);
        when(modelRepository.existsByName(modelName)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.createModel(modelBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelBindingModel, times(1)).getName();
        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(0)).getById(any());
        verify(engineService, times(0)).getEngineById(any());
        verify(modelRepository, times(0)).save(any());
    }

    @Test
    void createModelShouldThrowWhenBrandIdInvalid() {
        final String exceptionMessage = "message";
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(brandId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.createModel(modelBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelBindingModel, times(1)).getName();
        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(1)).getById(brandId);
        verify(engineService, times(0)).getEngineById(any());
        verify(modelRepository, times(0)).save(any());
    }

    @Test
    void createModelShouldThrowWhenEngineIdInvalid() {
        final String exceptionMessage = "message";
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).getEngineById(firstEngineId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.createModel(modelBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelBindingModel, times(1)).getName();
        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(1)).getById(brandId);
        verify(engineService, times(1)).getEngineById(firstEngineId);
        verify(modelRepository, times(0)).save(any());
    }
}