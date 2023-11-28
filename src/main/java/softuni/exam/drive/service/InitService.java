package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.repository.BrandRepository;

import java.util.List;

/**
 * Initialisation service creating initial brand records in the db
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class InitService implements CommandLineRunner {

    private final BrandRepository brandRepository;

    @Override
    public void run(String... args) {
        if (brandRepository.findAll().isEmpty()) {
            final Brand bmw = new Brand();
            bmw.setName("Bmw");
            final Brand mercedes = new Brand();
            mercedes.setName("Mercedes");
            final Brand audi = new Brand();
            audi.setName("Audi");

            brandRepository.saveAll(List.of(bmw, mercedes, audi));
        }
    }
}
