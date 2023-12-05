package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.repository.OfferRepository;

import java.io.IOException;

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

    public void createOffer(OfferBindingModel offerBindingModel) {
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
}
