package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.Model;

import java.util.List;

/**
 * Repository responsible for engine related CRUD operations
 * @author Vasil Mirchev
 */
public interface ModelRepository extends JpaRepository<Model, Long> {

    boolean existsByName(String name);

    List<Model> findAllByBrand(Brand brand);
}
