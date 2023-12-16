package softuni.exam.drive.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperConfigTest {

    @Test
    public void modelMapperShouldReturnNewMapper(){
        final ModelMapperConfig modelMapperConfig = new ModelMapperConfig();

        assertNotNull(modelMapperConfig.modelMapper());
    }

}