package softuni.exam.drive.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.enums.FuelType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vasil Mirchev
 */
@DataJpaTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
class EngineRepositoryTest {

    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void existsByNameShouldReturnTrue() {
        final String engineName = "M47TUD20";
        final Engine engine = new Engine();
        final Brand brand = new Brand();
        brand.setName("bwm");
        brandRepository.saveAndFlush(brand);
        engine.setName(engineName);
        engine.setHorsepower(150);
        engine.setDisplacement(1995);
        engine.setManufacturer(brandRepository.findById(1L).get());
        engine.setFuelType(FuelType.DIESEL);
        engineRepository.saveAndFlush(engine);

        assertThat(engineRepository.existsByName(engineName)).isTrue();
    }

    @Test
    void existsByNameShouldReturnFalse() {
        assertThat(engineRepository.existsByName("test")).isFalse();
    }
}