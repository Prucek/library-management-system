package cz.muni.fi.pa165.seminar3.librarymanagement;

import cz.muni.fi.pa165.seminar3.librarymanagement.seed.SeedService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * Class for initializing data in database.
 */
@Profile("!test")
@Component
public class DataInitializer implements ApplicationRunner {

    private final SeedService seedService;

    /**
     * Constructor for all used services.
     *
     * @param seedService seed service instance
     */
    public DataInitializer(SeedService seedService) {
        this.seedService = seedService;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedService.seed();
    }
}
