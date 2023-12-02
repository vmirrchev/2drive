package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.repository.EngineRepository;

import java.text.MessageFormat;
import java.util.List;

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
     * @throws RuntimeException if engine name is used
     */
    public void createEngine(EngineBindingModel engineBindingModel) {
        final String engineName = engineBindingModel.getName();

        if (engineRepository.existsByName(engineName)) {
            throw new RuntimeException(MessageFormat.format("There is already an engine with the given name ({0})", engineName));
        }

        final Brand brand = brandService.getById(engineBindingModel.getBrandId());
        final Engine engine = new Engine();
        engine.setBrand(brand);
        engine.setFuelType(engineBindingModel.getFuelType());
        engine.setName(engineName);
        engine.setDisplacement(engineBindingModel.getDisplacement());
        engine.setHorsepower(engineBindingModel.getHorsepower());

        engineRepository.save(engine);
    }

    /**
     * Get the engine object for the given id
     * @param engineId engine identifier
     * @return Engine with given id
     * @throws RuntimeException if engineId is invalid
     */
    public Engine getEngineById(Long engineId) {
        if (engineId == null) {
            throw new RuntimeException("Engine id cannot be null");
        }
        return engineRepository.findById(engineId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("There is no engine for the given id ({0})", engineId)));
    }

    /**
     * Get a list of engine objects for the given brand id
     * @param brandId brand identifier
     * @return List of engines
     * @throws RuntimeException if brandId is invalid
     */
    public List<Engine> getAllEnginesByBrandId(Long brandId) {
        final Brand brand = brandService.getById(brandId);
        return engineRepository.findAllByBrand(brand);
    }
}
