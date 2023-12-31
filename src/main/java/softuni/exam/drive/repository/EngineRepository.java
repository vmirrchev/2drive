package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Engine;

import java.util.List;

/**
 * Repository responsible for engine related CRUD operations
 * @author Vasil Mirchev
 */
public interface EngineRepository extends JpaRepository<Engine, Long> {

    boolean existsByName(String name);

    List<Engine> findAllByBrand(Brand brand);
}
