package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.repository.BrandRepository;

import java.text.MessageFormat;
import java.util.List;

/**
 * Service used for brand related operations
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    /**
     * Get the brand object for the given id
     * @param brandId brand identifier
     * @return Brand with given id
     * @throws RuntimeException if brandId is invalid
     */
    public Brand getById(Long brandId) {
        if (brandId == null) {
            throw new RuntimeException("Brand id cannot be null");
        }
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("There is no brand for the given id ({0})", brandId)));
    }

    /**
     * Get all brand objects
     * @return list of Brands
     */
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
