package softuni.exam.drive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.entity.Offer;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class OfferServiceTest extends BaseTest {

    private final OfferService offerService = new OfferService(modelService, engineService, offerRepository);

    private final List<Offer> offers = List.of(offer);

    @BeforeEach
    public void setUp() {
        when(offerBindingModel.getModelId()).thenReturn(modelId);
        when(offerBindingModel.getEngineId()).thenReturn(engineId);
        when(offerBindingModel.getPicture()).thenReturn(picture);
        when(offerBindingModel.getBodyType()).thenReturn(bodyType);
        when(offerBindingModel.getDriveType()).thenReturn(driveType);
        when(offerBindingModel.getTransmissionType()).thenReturn(transmissionType);
        when(offerBindingModel.getPrice()).thenReturn(price);
        when(offerBindingModel.getOdometer()).thenReturn(odometer);
        when(offerBindingModel.getYear()).thenReturn(year);
        when(offerBindingModel.getColor()).thenReturn(color);
        when(offerBindingModel.getDescription()).thenReturn(description);
        when(offerBindingModel.isHasServiceBook()).thenReturn(hasServiceBook);
        when(offerBindingModel.isHasAccidentDamage()).thenReturn(hasAccidentDamage);
    }

    @Test
    void createOfferShouldAddNewOfferWithPicture() throws IOException {
        final byte[] pictureBytes = new byte[]{82, 16, -17, 50, 90};
        final ArgumentCaptor<Offer> argumentCaptor = ArgumentCaptor.forClass(Offer.class);
        when(modelService.getModelById(modelId)).thenReturn(model);
        when(engineService.getEngineById(engineId)).thenReturn(engine);
        when(picture.getBytes()).thenReturn(pictureBytes);

        offerService.createOffer(offerBindingModel, user);

        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(engineId);
        verify(offerRepository).save(argumentCaptor.capture());
        final Offer offer = argumentCaptor.getValue();
        assertEquals(model, offer.getModel());
        assertEquals(engine, offer.getEngine());
        assertEquals(pictureBytes, offer.getPicture());
        assertEquals(bodyType, offer.getBodyType());
        assertEquals(driveType, offer.getDriveType());
        assertEquals(transmissionType, offer.getTransmissionType());
        assertEquals(price, offer.getPrice());
        assertEquals(odometer, offer.getOdometer());
        assertEquals(year, offer.getYear());
        assertEquals(color, offer.getColor());
        assertEquals(description, offer.getDescription());
        assertEquals(hasServiceBook, offer.isHasServiceBook());
        assertEquals(hasAccidentDamage, offer.isHasAccidentDamage());
    }

    @Test
    void createOfferShouldAddNewOfferWithoutPicture() throws IOException {
        final ArgumentCaptor<Offer> argumentCaptor = ArgumentCaptor.forClass(Offer.class);
        when(modelService.getModelById(modelId)).thenReturn(model);
        when(engineService.getEngineById(engineId)).thenReturn(engine);
        when(picture.getBytes()).thenReturn(new byte[]{});

        offerService.createOffer(offerBindingModel, user);

        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(engineId);
        verify(offerRepository).save(argumentCaptor.capture());
        final Offer offer = argumentCaptor.getValue();
        assertEquals(user, offer.getAddedBy());
        assertEquals(model, offer.getModel());
        assertEquals(engine, offer.getEngine());
        assertNull(offer.getPicture());
        assertEquals(bodyType, offer.getBodyType());
        assertEquals(driveType, offer.getDriveType());
        assertEquals(transmissionType, offer.getTransmissionType());
        assertEquals(price, offer.getPrice());
        assertEquals(odometer, offer.getOdometer());
        assertEquals(year, offer.getYear());
        assertEquals(color, offer.getColor());
        assertEquals(description, offer.getDescription());
        assertEquals(hasServiceBook, offer.isHasServiceBook());
        assertEquals(hasAccidentDamage, offer.isHasAccidentDamage());
    }

    @Test
    void createOfferShouldThrowWhenModelIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(modelService).getModelById(modelId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel, user));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(0)).getEngineById(any());
        verify(offerRepository, times(0)).save(any());
    }

    @Test
    void createOfferShouldThrowWhenEngineIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).getEngineById(engineId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel, user));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(any());
        verify(offerRepository, times(0)).save(any());
    }

    @Test
    void createOfferShouldThrowWhenImageCannotBeProcessed() throws IOException {
        when(modelService.getModelById(modelId)).thenReturn(model);
        when(engineService.getEngineById(engineId)).thenReturn(engine);
        doThrow(new IOException("")).when(picture).getBytes();

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel, user));

        assertEquals("Error processing offer image", thrown.getMessage());
        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(any());
        verify(offerRepository, times(0)).save(any());
    }

    @Test
    void getOfferByIdShouldReturnOffer() {
        when(offerRepository.findById(offerId)).thenReturn(Optional.ofNullable(offer));

        assertEquals(offer, offerService.getOfferById(offerId));
        verify(offerRepository, times(1)).findById(offerId);
    }

    @Test
    void getOfferByIdShouldThrowWhenOfferIdNull() {
        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.getOfferById(null));

        assertEquals(thrown.getMessage(), "Offer id cannot be null");
        verify(offerRepository, times(0)).findById(any());
    }

    @Test
    void getOfferByIdShouldThrowWhenOfferIdInvalid() {
        final String exceptionMessage = MessageFormat.format("There is no offer for the given id ({0})", offerId);
        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.getOfferById(offerId));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(offerRepository, times(1)).findById(offerId);
    }

    @Test
    void getAllOffersShouldReturnOffers() {
        when(offerRepository.findAll()).thenReturn(offers);

        final List<Offer> result = offerService.getAllOffers();

        assertEquals(offers, result);
        verify(offerRepository, times(1)).findAll();
    }

    @Test
    void getAllOffersByBodyTypeShouldReturnOffers() {
        when(offerRepository.findAllByBodyType(bodyType)).thenReturn(offers);

        final List<Offer> result = offerService.getAllOffersByBodyType(bodyType);

        assertEquals(offers, result);
        verify(offerRepository, times(1)).findAllByBodyType(bodyType);
    }

    @Test
    void getAllOffersByUserShouldReturnOffers() {
        when(offerRepository.findAllByAddedBy(user)).thenReturn(offers);

        final List<Offer> result = offerService.getAllOffersByUser(user);

        assertEquals(offers, result);
        verify(offerRepository, times(1)).findAllByAddedBy(user);
    }

    @Test
    void deleteOffer() {
        offerService.deleteOffer(offer);
        verify(offerRepository, times(1)).delete(offer);
    }
}