package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.ui.Model;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.model.dto.ModelBindingModel;
import softuni.exam.drive.model.dto.OfferBindingModel;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.FuelType;
import softuni.exam.drive.model.enums.TransmissionType;
import softuni.exam.drive.service.BrandService;
import softuni.exam.drive.service.OfferService;

import java.text.MessageFormat;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
@ExtendWith(OutputCaptureExtension.class)
class ViewControllerTest {

    private final BrandService brandService = mock(BrandService.class);
    private final OfferService offerService = mock(OfferService.class);
    private final ViewController viewController = new ViewController(brandService, offerService);
    private final Offer offer = mock(Offer.class);
    private final Model model = mock(Model.class);
    private final BodyType bodyType = mock(BodyType.class);
    private final String engineBindingModelAttribute = "engineBindingModel";
    private final String modelBindingModelAttribute = "modelBindingModel";
    private final String registerBindingModelAttribute = "registerBindingModel";
    private final String offerBindingModelAttribute = "offerBindingModel";
    private final String brandsAttribute = "brands";
    private final String offersAttribute = "offers";
    private final String offerAttribute = "offer";
    private final String fuelTypesAttribute = "fuelTypes";
    private final String bodyTypesAttribute = "bodyTypes";
    private final String driveTypesAttribute = "driveTypes";
    private final String transmissionTypesAttribute = "transmissionTypes";
    private final Long offerId = 1L;
    private final String addEnginePath = "add-engine";
    private final String addModelPath = "add-model";
    private final String registerPath = "register";
    private final String addOfferPath = "add-offer";

    @Test
    void getIndex() {
        final ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

        final String result = viewController.getIndex(model);

        verify(model).addAttribute(eq(bodyTypesAttribute), argumentCaptor.capture());
        final BodyType[] bodyTypes = (BodyType[]) argumentCaptor.getValue();
        assertThat(bodyTypes).containsAll(Arrays.asList(BodyType.values()));
        assertEquals("index", result);
    }

    @Test
    void getRegisterShouldUseNewRegisterBindingModel() {
        final ArgumentCaptor<RegisterBindingModel> argumentCaptor = ArgumentCaptor.forClass(RegisterBindingModel.class);
        when(model.containsAttribute(registerBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getRegister(model);

        verify(model).addAttribute(eq(registerBindingModelAttribute), argumentCaptor.capture());
        final RegisterBindingModel registerBindingModel = argumentCaptor.getValue();
        assertNull(registerBindingModel.getUsername());
        assertNull(registerBindingModel.getFirstName());
        assertNull(registerBindingModel.getLastName());
        assertNull(registerBindingModel.getEmail());
        assertNull(registerBindingModel.getPhoneNumber());
        assertNull(registerBindingModel.getPassword());
        assertEquals(registerPath, result);
    }

    @Test
    void getRegisterShouldNotUseNewRegisterBindingModel() {
        when(model.containsAttribute(registerBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getRegister(model);

        verify(model, times(0)).addAttribute(eq(registerBindingModelAttribute), any());
        assertEquals(registerPath, result);
    }

    @Test
    void getLogin() {
        assertEquals("login", viewController.getLogin(model));
    }

    @Test
    void getProfile() {
        assertEquals("profile", viewController.getProfile(model));
    }

    @Test
    void getAboutUs() {
        assertEquals("about-us", viewController.getAboutUs(model));
    }

    @Test
    void getPrivacyPolicy() {
        assertEquals("privacy-policy", viewController.getPrivacyPolicy(model));
    }

    @Test
    void getTermsAndConditions() {
        assertEquals("terms-and-conditions", viewController.getTermsAndConditions(model));
    }

    @Test
    void getSiteMap() {
        assertEquals("site-map", viewController.getSiteMap(model));
    }

    @Test
    void getContactUs() {
        assertEquals("contact-us", viewController.getContactUs(model));
    }

    @Test
    void getOffers() {
        assertEquals("offers", viewController.getOffers(model));
    }

    @Test
    void getFilteredOffers() {
        assertEquals("offers", viewController.getOffers(bodyType, model));
        verify(model).addAttribute(eq(offersAttribute), any());
    }

    @Test
    void getOffer(CapturedOutput capturedOutput) {
        ArgumentCaptor<Offer> argumentCaptor = ArgumentCaptor.forClass(Offer.class);
        when(offerService.getOfferById(offerId)).thenReturn(offer);

        final String result = viewController.getOffer(offerId, model);

        assertEquals("", capturedOutput.getOut());
        verify(model).addAttribute(eq(offerAttribute), argumentCaptor.capture());
        final Offer offer = argumentCaptor.getValue();
        assertNotNull(offer);
        assertEquals("offer", result);
    }

    @Test
    void getOfferShouldHandleExceptionsWhenOfferIdInvalid(CapturedOutput capturedOutput) {
        final String exceptionMessage = "message";
        ArgumentCaptor<Offer> argumentCaptor = ArgumentCaptor.forClass(Offer.class);
        doThrow(new RuntimeException(exceptionMessage)).when(offerService).getOfferById(offerId);

        final String result = viewController.getOffer(offerId, model);

        assertThat(capturedOutput).contains(MessageFormat.format("Offer retrieval operation failed. {0}", exceptionMessage));
        verify(model).addAttribute(eq(offerAttribute), argumentCaptor.capture());
        final Offer offer = argumentCaptor.getValue();
        assertNull(offer);
        assertEquals("offer", result);
    }

    @Test
    void getAddOfferShouldUseNewOfferBindingModel() {
        final ArgumentCaptor<OfferBindingModel> argumentCaptor = ArgumentCaptor.forClass(OfferBindingModel.class);
        when(model.containsAttribute(offerBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddOffer(model);

        verify(model).addAttribute(eq(offerBindingModelAttribute), argumentCaptor.capture());
        final OfferBindingModel registerBindingModel = argumentCaptor.getValue();
        assertNull(registerBindingModel.getModelId());
        assertNull(registerBindingModel.getEngineId());
        assertNull(registerBindingModel.getBodyType());
        assertNull(registerBindingModel.getDriveType());
        assertNull(registerBindingModel.getTransmissionType());
        assertNull(registerBindingModel.getTitle());
        assertNull(registerBindingModel.getColor());
        assertNull(registerBindingModel.getOdometer());
        assertNull(registerBindingModel.getYear());
        assertNull(registerBindingModel.getDescription());
        assertNull(registerBindingModel.getPrice());
        assertFalse(registerBindingModel.isHasServiceBook());
        assertFalse(registerBindingModel.isHasAccidentDamage());
        assertNull(registerBindingModel.getPicture());

        assertEquals(addOfferPath, result);
    }

    @Test
    void getAddOfferShouldNotUseNewOfferBindingModel() {
        when(model.containsAttribute(offerBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddOffer(model);

        verify(model, times(0)).addAttribute(eq(offerBindingModelAttribute), any());
        assertEquals(addOfferPath, result);
    }

    @Test
    void getAddEngineShouldUseNewEngineBindingModel() {
        final ArgumentCaptor<EngineBindingModel> argumentCaptor = ArgumentCaptor.forClass(EngineBindingModel.class);
        final ArgumentCaptor<Object> fuelTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddEngine(model);

        verify(model).addAttribute(eq(engineBindingModelAttribute), argumentCaptor.capture());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(fuelTypesAttribute), fuelTypesArgumentCaptor.capture());
        final EngineBindingModel engineBindingModel = argumentCaptor.getValue();
        assertNull(engineBindingModel.getDisplacement());
        assertNull(engineBindingModel.getFuelType());
        assertNull(engineBindingModel.getHorsepower());
        assertNull(engineBindingModel.getName());
        assertNull(engineBindingModel.getBrandId());
        final FuelType[] fuelTypes = (FuelType[]) fuelTypesArgumentCaptor.getValue();
        assertThat(fuelTypes).containsAll(Arrays.asList(FuelType.values()));
        assertEquals(addEnginePath, result);
    }

    @Test
    void getAddEngineShouldNotUseNewEngineBindingModel() {
        final ArgumentCaptor<Object> fuelTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddEngine(model);

        verify(model, times(0)).addAttribute(eq(engineBindingModelAttribute), any());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(fuelTypesAttribute), fuelTypesArgumentCaptor.capture());
        final FuelType[] fuelTypes = (FuelType[]) fuelTypesArgumentCaptor.getValue();
        assertThat(fuelTypes).containsAll(Arrays.asList(FuelType.values()));
        assertEquals(addEnginePath, result);
    }

    @Test
    void getMyOffers() {
        assertEquals("offers", viewController.getMyOffers(model));
    }

    @Test
    void getAddModel() {
        assertEquals("add-model", viewController.getAddModel(model));
    }

    @Test
    void getAddModelShouldUseNewModelBindingModel() {
        final ArgumentCaptor<ModelBindingModel> argumentCaptor = ArgumentCaptor.forClass(ModelBindingModel.class);
        final ArgumentCaptor<Object> bodyTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> driveTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> transmissionTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(model.containsAttribute(modelBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddModel(model);

        verify(model).addAttribute(eq(modelBindingModelAttribute), argumentCaptor.capture());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(bodyTypesAttribute), bodyTypesArgumentCaptor.capture());
        verify(model).addAttribute(eq(driveTypesAttribute), driveTypesArgumentCaptor.capture());
        verify(model).addAttribute(eq(transmissionTypesAttribute), transmissionTypesArgumentCaptor.capture());
        final ModelBindingModel modelBindingModel = argumentCaptor.getValue();
        assertNull(modelBindingModel.getName());
        assertNull(modelBindingModel.getBrandId());
        assertNull(modelBindingModel.getBodyTypes());
        assertNull(modelBindingModel.getDriveTypes());
        assertNull(modelBindingModel.getTransmissionTypes());
        assertNull(modelBindingModel.getEngineIds());
        assertNull(modelBindingModel.getStartYear());
        assertNull(modelBindingModel.getEndYear());
        final BodyType[] bodyTypes = (BodyType[]) bodyTypesArgumentCaptor.getValue();
        assertThat(bodyTypes).containsAll(Arrays.asList(BodyType.values()));
        final DriveType[] driveTypes = (DriveType[]) driveTypesArgumentCaptor.getValue();
        assertThat(driveTypes).containsAll(Arrays.asList(DriveType.values()));
        final TransmissionType[] transmissionTypes = (TransmissionType[]) transmissionTypesArgumentCaptor.getValue();
        assertThat(transmissionTypes).containsAll(Arrays.asList(TransmissionType.values()));
        assertEquals(addModelPath, result);
    }

    @Test
    void getAddModelShouldNotUseNewModelBindingModel() {
        final ArgumentCaptor<Object> bodyTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> driveTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> transmissionTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(model.containsAttribute(modelBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddModel(model);

        verify(model, times(0)).addAttribute(eq(modelBindingModelAttribute), any());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(bodyTypesAttribute), bodyTypesArgumentCaptor.capture());
        verify(model).addAttribute(eq(driveTypesAttribute), driveTypesArgumentCaptor.capture());
        verify(model).addAttribute(eq(transmissionTypesAttribute), transmissionTypesArgumentCaptor.capture());
        final BodyType[] bodyTypes = (BodyType[]) bodyTypesArgumentCaptor.getValue();
        assertThat(bodyTypes).containsAll(Arrays.asList(BodyType.values()));
        final DriveType[] driveTypes = (DriveType[]) driveTypesArgumentCaptor.getValue();
        assertThat(driveTypes).containsAll(Arrays.asList(DriveType.values()));
        final TransmissionType[] transmissionTypes = (TransmissionType[]) transmissionTypesArgumentCaptor.getValue();
        assertThat(transmissionTypes).containsAll(Arrays.asList(TransmissionType.values()));
        assertEquals(addModelPath, result);
    }
}