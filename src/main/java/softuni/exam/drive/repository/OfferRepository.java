package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.BodyType;

import java.util.List;

/**
 * Repository responsible for offer related CRUD operations
 * @author Vasil Mirchev
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findAllByBodyType(BodyType bodyType);

    List<Offer> findAllByAddedBy(User user);
}
