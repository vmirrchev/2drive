package softuni.exam.drive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class ModelServiceTest extends BaseTest {

    private final ModelService modelService = new ModelService(modelRepository, brandService, engineService);

    private final List<Long> engineIds = List.of(engineId);
    private final List<BodyType> bodyTypes = List.of(BodyType.SEDAN, BodyType.COUPE, BodyType.CONVERTIBLE, BodyType.WAGON);
    private final List<DriveType> driveTypes = List.of(DriveType.RWD, DriveType.AWD);
    private final List<TransmissionType> transmissionTypes = List.of(TransmissionType.MANUAL, TransmissionType.AUTOMATIC);
    private final Set<Engine> engines = Set.of(engine);

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

    @Test
    void createModelShouldAddNewModel() {
        final ArgumentCaptor<Model> argumentCaptor = ArgumentCaptor.forClass(Model.class);
        when(brandService.getById(any())).thenReturn(brand);
        when(engineService.getEngineById(engineId)).thenReturn(engine);

        modelService.createModel(modelBindingModel);

        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(1)).getById(brandId);
        verify(engineService, times(1)).getEngineById(engineId);
        verify(modelRepository).save(argumentCaptor.capture());
        final Model model = argumentCaptor.getValue();
        assertEquals(modelName, model.getName());
        assertEquals(startYear, model.getStartYear());
        assertEquals(endYear, model.getEndYear());
        assertEquals(bodyTypes, model.getBodyTypes());
        assertEquals(driveTypes, model.getDriveTypes());
        assertEquals(transmissionTypes, model.getTransmissionTypes());
        assertEquals(brand, model.getBrand());
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
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).getEngineById(engineId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.createModel(modelBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelBindingModel, times(1)).getName();
        verify(modelRepository, times(1)).existsByName(modelName);
        verify(brandService, times(1)).getById(brandId);
        verify(engineService, times(1)).getEngineById(engineId);
        verify(modelRepository, times(0)).save(any());
    }

    @Test
    void getModelByIdShouldReturnModel() {
        final Model model = mock(Model.class);
        when(modelRepository.findById(modelId)).thenReturn(Optional.ofNullable(model));

        assertEquals(model, modelService.getModelById(modelId));
        verify(modelRepository, times(1)).findById(modelId);
    }

    @Test
    void getModelByIdShouldThrowWhenIdINull() {
        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.getModelById(null));

        assertEquals(thrown.getMessage(), "Model id cannot be null");
        verify(modelRepository, times(0)).findById(any());
    }

    @Test
    void getModelByIdShouldThrowWhenIdInvalid() {
        final String exceptionMessage = MessageFormat.format("There is no car model for the given id ({0})", modelId);
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.getModelById(modelId));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelRepository, times(1)).findById(modelId);
    }

    @Test
    void getAllModelsByBrandIdShouldReturnModels() {
        final List<Model> models = List.of(model);
        when(brandService.getById(brandId)).thenReturn(brand);
        when(modelRepository.findAllByBrand(brand)).thenReturn(models);

        final List<Model> result = modelService.getAllModelsByBrandId(brandId);

        assertEquals(models, result);
        verify(brandService, times(1)).getById(brandId);
        verify(modelRepository, times(1)).findAllByBrand(brand);
    }

    @Test
    void getAllModelsByBrandIdShouldThrowWhenBrandIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(brandService).getById(brandId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> modelService.getAllModelsByBrandId(brandId));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(brandService, times(1)).getById(brandId);
        verify(modelRepository, times(0)).findAllByBrand(any());
    }
}