package softuni.exam.drive;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.exam.drive.model.dto.*;
import softuni.exam.drive.model.entity.*;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.FuelType;
import softuni.exam.drive.model.enums.TransmissionType;
import softuni.exam.drive.repository.*;
import softuni.exam.drive.service.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * Base class with mocks and constants
 */
public class BaseTest {

    // Instances
    protected final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    protected final ModelMapper modelMapper = new ModelMapper();

    // Service mocks
    protected final EngineService engineService = mock(EngineService.class);
    protected final ModelService modelService = mock(ModelService.class);
    protected final OfferService offerService = mock(OfferService.class);
    protected final UserService userService = mock(UserService.class);
    protected final BrandService brandService = mock(BrandService.class);

    // Binding model mocks
    protected final EngineBindingModel engineBindingModel = mock(EngineBindingModel.class);
    protected final ModelBindingModel modelBindingModel = mock(ModelBindingModel.class);
    protected final OfferBindingModel offerBindingModel = mock(OfferBindingModel.class);
    protected final RegisterBindingModel registerBindingModel = mock(RegisterBindingModel.class);
    protected final UserBindingModel userBindingModel = mock(UserBindingModel.class);
    protected final RoleBindingModel roleBindingModel = mock(RoleBindingModel.class);

    // Entity mocks
    protected final Model model = mock(Model.class);
    protected final User user = mock(User.class);
    protected final Offer offer = mock(Offer.class);
    protected final Brand brand = mock(Brand.class);
    protected final Engine engine = mock(Engine.class);

    // Repository mocks
    protected final UserRepository userRepository = mock(UserRepository.class);
    protected final OfferRepository offerRepository = mock(OfferRepository.class);
    protected final ModelRepository modelRepository = mock(ModelRepository.class);
    protected final BrandRepository brandRepository = mock(BrandRepository.class);
    protected final EngineRepository engineRepository = mock(EngineRepository.class);

    // Other mocks
    protected final Authentication authentication = mock(Authentication.class);
    protected final BindingResult bindingResult = mock(BindingResult.class);
    protected final RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
    protected final HttpServletRequest http = mock(HttpServletRequest.class);
    protected final MultipartFile picture = mock(MultipartFile.class);

    // Constants
    protected final Long userId = 1L;
    protected final String username = "johnDoe";
    protected final String firstName = "John";
    protected final String lastName = "Doe";
    protected final String email = "john_doe@yahoo.com";
    protected final String phoneNumber = "0888504030";
    protected final String password = "password";
    protected final Long brandId = 1L;
    protected final Long modelId = 1L;
    protected final Long offerId = 1L;
    protected final Long engineId = 1L;
    protected final FuelType fuelType = FuelType.DIESEL;
    protected final BodyType bodyType = BodyType.SEDAN;
    protected final DriveType driveType = DriveType.RWD;
    protected final TransmissionType transmissionType = TransmissionType.MANUAL;
    protected final String engineName = "M47TUD20";
    protected final Integer displacement = 1995;
    protected final Integer horsepower = 150;
    protected final int price = 10_000;
    protected final int odometer = 265_500;
    protected final int year = 2007;
    protected final String color = "blue";
    protected final String description = "Car is in perfect condition. No damage, has new tires and 17' alloy wheels";
    protected final boolean hasServiceBook = true;
    protected final boolean hasAccidentDamage = false;
    protected final String modelName = "3 Series";
    protected final int startYear = 2004;
    protected final int endYear = 2010;

    protected final String addEnginePath = "add-engine";
    protected final String addModelPath = "add-model";
    protected final String registerPath = "register";
    protected final String addOfferPath = "add-offer";
    protected final String profilePath = "profile";
    protected final String loginPath = "login";
    protected final String offersPath = "offers";
    protected final String offerPath = "offer";
    protected final String rolesPath = "roles";
    protected final String editProfilePath = "edit-profile";
    protected final String editRolePath = "edit-role";
    protected final String redirectPath = "redirect:/";
    protected final String redirectRegisterUrl = redirectPath + registerPath;
    protected final String redirectLoginUrl = redirectPath + loginPath;
    protected final String redirectProfileUrl = redirectPath + profilePath;
    protected final String redirectEditProfileUrl = redirectPath + editProfilePath;
    protected final String redirectRolesUrl = redirectPath + rolesPath;
    protected final String redirectAddEngineUrl = redirectPath + addEnginePath;
    protected final String redirectAddModelUrl = redirectPath + addModelPath;
    protected final String redirectAddOfferUrl = redirectPath + addOfferPath;
    protected final String redirectOffersUrl = redirectPath + offersPath;

    protected final String engineBindingModelAttribute = "engineBindingModel";
    protected final String modelBindingModelAttribute = "modelBindingModel";
    protected final String registerBindingModelAttribute = "registerBindingModel";
    protected final String offerBindingModelAttribute = "offerBindingModel";
    protected final String userBindingModelAttribute = "userBindingModel";
    protected final String roleBindingModelAttribute = "roleBindingModel";
    protected final String brandsAttribute = "brands";
    protected final String offersAttribute = "offers";
    protected final String offerAttribute = "offer";
    protected final String fuelTypesAttribute = "fuelTypes";
    protected final String bodyTypesAttribute = "bodyTypes";
    protected final String driveTypesAttribute = "driveTypes";
    protected final String transmissionTypesAttribute = "transmissionTypes";
    protected final String userIdAttribute = "userId";
    protected final String userAttribute = "user";
    protected final String rolesAttribute = "roles";

    protected final String exceptionMessage = "Exception message";

    @AfterEach
    public void clean() {
        reset(engineService, modelService, offerService, userService, brandService);
        reset(engineBindingModel, modelBindingModel, offerBindingModel, registerBindingModel, userBindingModel, roleBindingModel);
        reset(userRepository, offerRepository, modelRepository, brandRepository, engineRepository);
        reset(authentication, bindingResult, redirectAttributes, http, picture);
    }

}
