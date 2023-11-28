package softuni.exam.drive.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;
import softuni.exam.drive.model.dto.EngineBindingModel;
import softuni.exam.drive.service.BrandService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class ViewControllerTest {

    private final BrandService brandService = mock(BrandService.class);
    private final ViewController viewController = new ViewController(brandService);
    private final Model model = mock(Model.class);
    private final String engineBindingModelAttribute = "engineBindingModel";
    private final String brandsAttribute = "brands";
    private final String fuelTypesAttribute = "fuelTypes";
    private final String addEnginePath = "add-engine";

    @Test
    void getIndex() {
        assertEquals("index", viewController.getIndex(model));
    }

    @Test
    void getRegister() {
        assertEquals("register", viewController.getRegister(model));
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
    void getOffer() {
        assertEquals("offer", viewController.getOffer(model));
    }

    @Test
    void getAddOffer() {
        assertEquals("add-offer", viewController.getAddOffer(model));
    }

    @Test
    void getAddEngineShouldUseNewEngineBindingModel() {
        final ArgumentCaptor<EngineBindingModel> argumentCaptor = ArgumentCaptor.forClass(EngineBindingModel.class);
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddEngine(model);

        verify(model).addAttribute(eq(engineBindingModelAttribute), argumentCaptor.capture());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(fuelTypesAttribute), any());
        final EngineBindingModel engineBindingModel = argumentCaptor.getValue();
        assertNull(engineBindingModel.getDisplacement());
        assertNull(engineBindingModel.getFuelType());
        assertNull(engineBindingModel.getHorsepower());
        assertNull(engineBindingModel.getName());
        assertNull(engineBindingModel.getBrandId());
        assertEquals(addEnginePath, result);
    }

    @Test
    void getAddEngineShouldNotUseNewEngineBindingModel() {
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddEngine(model);

        verify(model, times(0)).addAttribute(eq(engineBindingModelAttribute), any());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(fuelTypesAttribute), any());
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
}