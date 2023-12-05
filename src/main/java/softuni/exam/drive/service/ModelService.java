package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.repository.ModelRepository;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service used for model related operations
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final BrandService brandService;
    private final EngineService engineService;

    /**
     * Creates and saves new car model to the database
     * @param modelBindingModel car model dto
     * @throws RuntimeException if model name is used
     */
    public void createModel(final ModelBindingModel modelBindingModel) {
        final String modelName = modelBindingModel.getName();

        if (modelRepository.existsByName(modelName)) {
            throw new RuntimeException(MessageFormat.format("There is already a model with the given name ({0})", modelName));
        }

        final Brand brand = brandService.getById(modelBindingModel.getBrandId());
        final Set<Engine> engines = new HashSet<>();

        for (final Long engineId : modelBindingModel.getEngineIds()) {
            engines.add(engineService.getEngineById(engineId));
        }

        final Model model = new Model();
        model.setBrand(brand);
        model.setEngines(engines);
        model.setBodyTypes(modelBindingModel.getBodyTypes());
        model.setDriveTypes(modelBindingModel.getDriveTypes());
        model.setTransmissionTypes(modelBindingModel.getTransmissionTypes());
        model.setName(modelBindingModel.getName());
        model.setStartYear(modelBindingModel.getStartYear());
        model.setEndYear(modelBindingModel.getEndYear());

        modelRepository.save(model);
    }

    /**
     * Get the car model object for the given id
     * @param modelId model identifier
     * @return Model with given id
     * @throws RuntimeException if modelId is invalid
     */
    public Model getModelById(Long modelId) {
        if (modelId == null) {
            throw new RuntimeException("Model id cannot be null");
        }
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("There is no car model for the given id ({0})", modelId)));
    }

    /**
     * Get a list of model objects for the given brand id
     * @param brandId brand identifier
     * @return List of models
     * @throws RuntimeException if brandId is invalid
     */
    public List<Model> getAllModelsByBrandId(Long brandId) {
        final Brand brand = brandService.getById(brandId);
        return modelRepository.findAllByBrand(brand);
    }
}
