package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.drive.model.entity.Offer;

/**
 * Repository responsible for offer related CRUD operations
 * @author Vasil Mirchev
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
