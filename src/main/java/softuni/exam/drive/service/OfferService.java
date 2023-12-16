package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.repository.OfferRepository;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Service used for offer related operations
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class OfferService {

    private final ModelService modelService;
    private final EngineService engineService;
    private final OfferRepository offerRepository;

    /**
     * Creates offer for the given binding model object
     * @param offerBindingModel binding model representing the offer dto
     * @throws RuntimeException when picture bytes can't be retrieved
     */
    public void createOffer(final OfferBindingModel offerBindingModel, final User user) {
        final Model model = modelService.getModelById(offerBindingModel.getModelId());
        final Engine engine = engineService.getEngineById(offerBindingModel.getEngineId());

        byte[] pictureBytes;

        try {
            pictureBytes = offerBindingModel.getPicture().getBytes();
            if (pictureBytes.length == 0) {
                pictureBytes = null;
            }
        } catch (IOException ioException) {
            throw new RuntimeException("Error processing offer image");
        }

        final Offer offer = new Offer();
        offer.setAddedBy(user);
        offer.setModel(model);
        offer.setEngine(engine);
        offer.setBodyType(offerBindingModel.getBodyType());
        offer.setDriveType(offerBindingModel.getDriveType());
        offer.setTransmissionType(offerBindingModel.getTransmissionType());
        offer.setTitle(offerBindingModel.getTitle());
        offer.setPrice(offerBindingModel.getPrice());
        offer.setOdometer(offerBindingModel.getOdometer());
        offer.setYear(offerBindingModel.getYear());
        offer.setColor(offerBindingModel.getColor());
        offer.setDescription(offerBindingModel.getDescription());
        offer.setHasServiceBook(offerBindingModel.isHasServiceBook());
        offer.setHasAccidentDamage(offerBindingModel.isHasAccidentDamage());
        offer.setPicture(pictureBytes);

        offerRepository.save(offer);
    }

    /**
     * Get the offer object for the given id
     * @param offerId engine identifier
     * @return Offer with given id
     * @throws RuntimeException if offerId is invalid
     */
    public Offer getOfferById(Long offerId) {
        if (offerId == null) {
            throw new RuntimeException("Offer id cannot be null");
        }
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("There is no offer for the given id ({0})", offerId)));
    }

    /**
     * Get a list of offers
     * @return List with all offers
     */
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    /**
     * Get a list of offers with given body type
     * @param bodyType body type of the vehicle
     * @return List with all offers
     */
    public List<Offer> getAllOffersByBodyType(BodyType bodyType) {
        return offerRepository.findAllByBodyType(bodyType);
    }

    /**
     * Get a list of offers with given creator
     * @param user creator of the offer
     * @return List with all offers
     */
    public List<Offer> getAllOffersByUser(User user) {
        return offerRepository.findAllByAddedBy(user);
    }
}
