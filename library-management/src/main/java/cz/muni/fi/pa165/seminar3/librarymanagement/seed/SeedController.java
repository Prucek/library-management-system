package cz.muni.fi.pa165.seminar3.librarymanagement.seed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class representing seeding controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping("/seed")
public class SeedController {

    public final SeedService seedService;

    /**
     * Creates a new seed controller instance.
     *
     * @param seedService seed service instance
     */
    @Autowired
    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    /**
     * Seeds the database.
     */
    @Operation(summary = "Seeds the database")
    @ApiResponse(responseCode = "204", description = "The DB has been seeded")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void seed() {
        seedService.seed();
    }

    /**
     * Drops the database.
     */
    @Operation(summary = "Drops the database")
    @ApiResponse(responseCode = "204", description = "Database dropped")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void drop() {
        seedService.drop();
    }
}
