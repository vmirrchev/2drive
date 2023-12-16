package softuni.exam.drive.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import softuni.exam.drive.model.dto.*;
import softuni.exam.drive.model.entity.Offer;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.*;
import softuni.exam.drive.service.BrandService;
import softuni.exam.drive.service.OfferService;
import softuni.exam.drive.service.UserService;

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
    private final UserService userService = mock(UserService.class);
    private final ViewController viewController = new ViewController(brandService, offerService, userService);
    private final HttpServletRequest http = mock(HttpServletRequest.class);
    private final Authentication authentication = mock(Authentication.class);
    private final Offer offer = mock(Offer.class);
    private final Model model = mock(Model.class);
    private final BodyType bodyType = mock(BodyType.class);
    final User user = mock(User.class);
    final User dbUser = mock(User.class);
    private final String engineBindingModelAttribute = "engineBindingModel";
    private final String modelBindingModelAttribute = "modelBindingModel";
    private final String registerBindingModelAttribute = "registerBindingModel";
    private final String offerBindingModelAttribute = "offerBindingModel";
    private final String userBindingModelAttribute = "userBindingModel";
    private final String roleBindingModelAttribute = "roleBindingModel";
    private final String brandsAttribute = "brands";
    private final String offersAttribute = "offers";
    private final String offerAttribute = "offer";
    private final String fuelTypesAttribute = "fuelTypes";
    private final String bodyTypesAttribute = "bodyTypes";
    private final String driveTypesAttribute = "driveTypes";
    private final String transmissionTypesAttribute = "transmissionTypes";
    private final String userIdAttribute = "userId";
    private final String userAttribute = "user";
    private final String rolesAttribute = "roles";
    private final Long offerId = 1L;
    private final Long userId = 1L;
    private final String addEnginePath = "add-engine";
    private final String addModelPath = "add-model";
    private final String registerPath = "register";
    private final String addOfferPath = "add-offer";
    private final String profilePath = "profile";
    private final String loginPath = "login";
    private final String offersPath = "offers";
    private final String offerPath = "offer";
    private final String redirectIndexPath = "redirect:/";
    private final String redirectLoginPath = "redirect:/login";
    private final String editProfilePath = "edit-profile";
    private final String editRolePath = "edit-role";

    @AfterEach
    public void clean() {
        reset(brandService, offerService, http, authentication);
    }

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
    void getRegisterShouldRedirectWhenAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(true);

        assertEquals(redirectIndexPath, viewController.getRegister(model, authentication));
    }

    @Test
    void getRegisterShouldUseNewRegisterBindingModel() {
        final ArgumentCaptor<RegisterBindingModel> argumentCaptor = ArgumentCaptor.forClass(RegisterBindingModel.class);
        when(model.containsAttribute(registerBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getRegister(model, authentication);

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

        final String result = viewController.getRegister(model, authentication);

        verify(model, times(0)).addAttribute(eq(registerBindingModelAttribute), any());
        assertEquals(registerPath, result);
    }

    @Test
    void getLoginShouldRedirectWhenAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(true);
        assertEquals(redirectIndexPath, viewController.getLogin(model, authentication));
    }

    @Test
    void getLoginShouldReturnLoginWhenAuthenticationNull() {
        assertEquals(loginPath, viewController.getLogin(model, null));
    }

    @Test
    void getLoginShouldReturnLoginWhenNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);
        assertEquals(loginPath, viewController.getLogin(model, authentication));
    }

    @Test
    void getLoginError() {
        assertEquals(loginPath, viewController.getLoginError(model));
        verify(model, times(1)).addAttribute("loginError", true);
    }

    @Test
    void getProfileShouldRedirectWhenAuthenticationNull() {
        assertEquals(redirectLoginPath, viewController.getProfile(model, null));
    }

    @Test
    void getProfileShouldReturnProfile() {
        final User user = mock(User.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(dbUser);

        final String result = viewController.getProfile(model, authentication);

        verify(authentication, times(1)).getPrincipal();
        verify(model, times(1)).addAttribute("username", dbUser.getUsername());
        verify(model, times(1)).addAttribute("firstName", dbUser.getFirstName());
        verify(model, times(1)).addAttribute("lastName", dbUser.getLastName());
        verify(model, times(1)).addAttribute("email", dbUser.getEmail());
        verify(model, times(1)).addAttribute("phoneNumber", dbUser.getPhoneNumber());
        assertEquals(profilePath, result);
    }

    @Test
    void getEditProfileShouldRedirectWhenAuthenticationNull() {
        assertEquals(redirectLoginPath, viewController.getEditProfile(model, null));
    }

    @Test
    void getEditProfileShouldUseNewUserBindingModel() {
        ArgumentCaptor<UserBindingModel> argumentCaptor = ArgumentCaptor.forClass(UserBindingModel.class);
        final String username = "john_doe";
        final String firstName = "John";
        final String lastName = "Doe";
        final String email = "john_doe87@yahoo.com";
        final String phoneNumber = "0888908070";
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(dbUser);
        when(model.containsAttribute(eq(userBindingModelAttribute))).thenReturn(false);
        when(dbUser.getUsername()).thenReturn(username);
        when(dbUser.getFirstName()).thenReturn(firstName);
        when(dbUser.getLastName()).thenReturn(lastName);
        when(dbUser.getEmail()).thenReturn(email);
        when(dbUser.getPhoneNumber()).thenReturn(phoneNumber);

        final String result = viewController.getEditProfile(model, authentication);

        verify(authentication, times(1)).getPrincipal();
        verify(userService, times(1)).getUserById(userId);
        verify(model, times(1)).addAttribute(eq(userIdAttribute), eq(userId));
        verify(model, times(1)).addAttribute(eq(userBindingModelAttribute), argumentCaptor.capture());
        final UserBindingModel userBindingModel = argumentCaptor.getValue();
        assertEquals(username, userBindingModel.getUsername());
        assertEquals(firstName, userBindingModel.getFirstName());
        assertEquals(lastName, userBindingModel.getLastName());
        assertEquals(email, userBindingModel.getEmail());
        assertEquals(phoneNumber, userBindingModel.getPhoneNumber());
        assertEquals(editProfilePath, result);
    }

    @Test
    void getEditProfileShouldNotUseNewUserBindingModel() {
        final User user = mock(User.class);
        final User dbUser = mock(User.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(dbUser);
        when(model.containsAttribute(eq(userBindingModelAttribute))).thenReturn(true);

        final String result = viewController.getEditProfile(model, authentication);

        verify(authentication, times(1)).getPrincipal();
        verify(userService, times(1)).getUserById(userId);
        verify(model, times(1)).addAttribute(eq(userIdAttribute), eq(userId));
        verify(model, times(0)).addAttribute(eq(userBindingModelAttribute), any());
        assertEquals(editProfilePath, result);
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
        assertEquals(offersPath, viewController.getOffers(model));
    }

    @Test
    void getFilteredOffers() {
        assertEquals(offersPath, viewController.getOffers(bodyType, model));
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
        assertEquals(offerPath, result);
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
        assertEquals(offerPath, result);
    }

    @Test
    void getAddOfferShouldRedirectWhenAuthenticationNull() {
        assertEquals(redirectLoginPath, viewController.getAddOffer(model, null));
    }

    @Test
    void getAddOfferShouldUseNewOfferBindingModel() {
        final ArgumentCaptor<OfferBindingModel> argumentCaptor = ArgumentCaptor.forClass(OfferBindingModel.class);
        when(model.containsAttribute(offerBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddOffer(model, authentication);

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

        final String result = viewController.getAddOffer(model, authentication);

        verify(model, times(0)).addAttribute(eq(offerBindingModelAttribute), any());
        assertEquals(addOfferPath, result);
    }

    @Test
    void getAddEngineShouldRedirectWhenUserNotAdmin() {
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(false);
        assertEquals(redirectIndexPath, viewController.getAddEngine(model, http));
    }

    @Test
    void getAddEngineShouldUseNewEngineBindingModel() {
        final ArgumentCaptor<EngineBindingModel> argumentCaptor = ArgumentCaptor.forClass(EngineBindingModel.class);
        final ArgumentCaptor<Object> fuelTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddEngine(model, http);

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
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(engineBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddEngine(model, http);

        verify(model, times(0)).addAttribute(eq(engineBindingModelAttribute), any());
        verify(model).addAttribute(eq(brandsAttribute), any());
        verify(model).addAttribute(eq(fuelTypesAttribute), fuelTypesArgumentCaptor.capture());
        final FuelType[] fuelTypes = (FuelType[]) fuelTypesArgumentCaptor.getValue();
        assertThat(fuelTypes).containsAll(Arrays.asList(FuelType.values()));
        assertEquals(addEnginePath, result);
    }

    @Test
    void getMyOffersShouldRedirectWhenAuthenticationNull() {
        assertEquals(redirectLoginPath, viewController.getMyOffers(model, null));
        verify(authentication, times(0)).getPrincipal();
    }

    @Test
    void getMyOffersShouldReturnOffers() {
        assertEquals(offersPath, viewController.getMyOffers(model, authentication));
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    void getAddModelShouldRedirectWhenUserNotAdmin() {
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(false);
        assertEquals(redirectIndexPath, viewController.getAddModel(model, http));
    }

    @Test
    void getAddModelShouldUseNewModelBindingModel() {
        final ArgumentCaptor<ModelBindingModel> argumentCaptor = ArgumentCaptor.forClass(ModelBindingModel.class);
        final ArgumentCaptor<Object> bodyTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> driveTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        final ArgumentCaptor<Object> transmissionTypesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(modelBindingModelAttribute)).thenReturn(false);

        final String result = viewController.getAddModel(model, http);

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
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(modelBindingModelAttribute)).thenReturn(true);

        final String result = viewController.getAddModel(model, http);

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

    @Test
    void getUserRolesShouldRedirectWhenUserNotAdmin() {
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(false);
        assertEquals(redirectIndexPath, viewController.getUserRoles(model, http));
    }

    @Test
    void getUserRolesShouldReturnRoles() {
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);

        assertEquals("roles", viewController.getUserRoles(model, http));
        verify(model, times(1)).addAttribute("users", userService.getAllUsers());
    }

    @Test
    void getEditUserRolesShouldRedirectWhenUserNotAdmin() {
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(false);
        assertEquals(redirectIndexPath, viewController.getEditUserRoles(userId, model, http));
    }

    @Test
    void getEditUserRolesShouldUseNewModelBindingModel() {
        final ArgumentCaptor<RoleBindingModel> argumentCaptor = ArgumentCaptor.forClass(RoleBindingModel.class);
        final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        final ArgumentCaptor<Object> rolesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(roleBindingModelAttribute)).thenReturn(false);
        when(userService.getUserById(userId)).thenReturn(user);

        final String result = viewController.getEditUserRoles(userId, model, http);

        verify(userService, times(1)).getUserById(userId);
        verify(model).addAttribute(eq(roleBindingModelAttribute), argumentCaptor.capture());
        verify(model).addAttribute(eq(userAttribute), userArgumentCaptor.capture());
        verify(model).addAttribute(eq(rolesAttribute), rolesArgumentCaptor.capture());
        final RoleBindingModel roleBindingModel = argumentCaptor.getValue();
        assertNull(roleBindingModel.getRole());
        final Role[] roles = (Role[]) rolesArgumentCaptor.getValue();
        assertThat(roles).containsAll(Arrays.asList(Role.values()));
        assertEquals(editRolePath, result);
    }

    @Test
    void getEditUserRolesShouldNotUseNewModelBindingModel() {
        final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        final ArgumentCaptor<Object> rolesArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        when(http.isUserInRole(Role.ROLE_ADMIN.name())).thenReturn(true);
        when(model.containsAttribute(roleBindingModelAttribute)).thenReturn(true);
        when(userService.getUserById(userId)).thenReturn(user);

        final String result = viewController.getEditUserRoles(userId, model, http);

        verify(userService, times(1)).getUserById(userId);
        verify(model, times(0)).addAttribute(eq(roleBindingModelAttribute), any());
        verify(model).addAttribute(eq(userAttribute), userArgumentCaptor.capture());
        verify(model).addAttribute(eq(rolesAttribute), rolesArgumentCaptor.capture());
        final Role[] roles = (Role[]) rolesArgumentCaptor.getValue();
        assertThat(roles).containsAll(Arrays.asList(Role.values()));
        assertEquals(editRolePath, result);
    }
}