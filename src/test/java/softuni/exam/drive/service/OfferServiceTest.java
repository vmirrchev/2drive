package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.entity.Model;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;
import softuni.exam.drive.repository.OfferRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class OfferServiceTest {

    private final ModelService modelService = mock(ModelService.class);
    private final EngineService engineService = mock(EngineService.class);
    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OfferService offerService = new OfferService(modelService, engineService, offerRepository);
    private final OfferBindingModel offerBindingModel = mock(OfferBindingModel.class);
    private final Model model = mock(Model.class);
    private final Engine engine = mock(Engine.class);
    private final MultipartFile picture = mock(MultipartFile.class);
    private final Long modelId = 1L;
    private final Long engineId = 1L;
    private final BodyType bodyType = BodyType.SEDAN;
    private final DriveType driveType = DriveType.RWD;
    private final TransmissionType transmissionType = TransmissionType.MANUAL;
    private final int price = 10_000;
    private final int odometer = 265_500;
    private final int year = 2007;
    private final String color = "blue";
    private final String description = "Car is in perfect condition. No damage, has new tires and 17' alloy wheels";
    private final boolean hasServiceBook = true;
    private final boolean hasAccidentDamage = false;
    private final String exceptionMessage = "message";

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

    @AfterEach
    public void clean() {
        reset(modelService, engineService, offerRepository);
    }

    @Test
    void createOfferShouldAddNewOfferWithPicture() throws IOException {
        final byte[] pictureBytes = new byte[]{82, 16, -17, 50, 90};
        final ArgumentCaptor<Offer> argumentCaptor = ArgumentCaptor.forClass(Offer.class);
        when(modelService.getModelById(modelId)).thenReturn(model);
        when(engineService.getEngineById(engineId)).thenReturn(engine);
        when(picture.getBytes()).thenReturn(pictureBytes);

        offerService.createOffer(offerBindingModel);

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

        offerService.createOffer(offerBindingModel);

        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(engineId);
        verify(offerRepository).save(argumentCaptor.capture());
        final Offer offer = argumentCaptor.getValue();
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

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(0)).getEngineById(any());
        verify(offerRepository, times(0)).save(any());
    }

    @Test
    void createOfferShouldThrowWhenEngineIdInvalid() {
        doThrow(new RuntimeException(exceptionMessage)).when(engineService).getEngineById(engineId);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel));

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

        final Throwable thrown = assertThrows(RuntimeException.class, () -> offerService.createOffer(offerBindingModel));

        assertEquals("Error processing offer image", thrown.getMessage());
        verify(modelService, times(1)).getModelById(modelId);
        verify(engineService, times(1)).getEngineById(any());
        verify(offerRepository, times(0)).save(any());
    }
}