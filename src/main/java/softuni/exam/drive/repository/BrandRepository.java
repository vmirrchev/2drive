package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.drive.model.entity.Brand;

/**
 * Repository responsible for brand related CRUD operations
 * @author Vasil Mirchev
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
