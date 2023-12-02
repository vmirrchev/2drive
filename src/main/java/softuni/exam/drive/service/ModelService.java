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
        model.setTransmissions(modelBindingModel.getTransmissionTypes());
        model.setName(modelBindingModel.getName());
        model.setStartYear(modelBindingModel.getStartYear());
        model.setEndYear(modelBindingModel.getEndYear());

        modelRepository.save(model);
    }
}
