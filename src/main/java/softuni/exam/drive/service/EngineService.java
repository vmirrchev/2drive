package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.repository.EngineRepository;

import java.text.MessageFormat;

/**
 * Service used for brand related operations
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class EngineService {

    private final EngineRepository engineRepository;
    private final BrandService brandService;

    /**
     * Creates and saves new engine to the database
     * @param engineBindingModel engine dto
     * @throws RuntimeException engine name is used
     */
    public void createEngine(EngineBindingModel engineBindingModel) {
        final Brand brand = brandService.getById(engineBindingModel.getBrandId());
        final String engineName = engineBindingModel.getName();

        if (engineRepository.existsByName(engineName)) {
            throw new RuntimeException(MessageFormat.format("There is already an engine with the given name ({0})", engineName));
        }

        final Engine engine = new Engine();
        engine.setManufacturer(brand);
        engine.setFuelType(engineBindingModel.getFuelType());
        engine.setName(engineName);
        engine.setDisplacement(engineBindingModel.getDisplacement());
        engine.setHorsepower(engineBindingModel.getHorsepower());

        engineRepository.save(engine);
    }
}
