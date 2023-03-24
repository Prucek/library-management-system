package cz.muni.fi.pa165.seminar3.reporting.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@OpenAPIDefinition(
        info = @Info(title = "RestAPI controller for generating report stats",
                version = "1.1",
                description = """
                        The API has operations for:
                        - getting total count of fines ant total paid fines for user
                        - getting total count of users in system
                        - getting count of borrowed and returned books of specific user
                        """,
                contact = @Contact(name = "Marek Miček", email = "540461@mail.muni.cz", url = "https://is.muni.cz/auth/osoba/540461")
        )
)
@RequestMapping("/reports")
public class ReportController {

    private final ReportService service;

    @Autowired
    public ReportController(ReportService service) {
        this.service = service;
    }

    /**
     * REST method for getting finance report of specific user.
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto with finance report info
     */
    @Operation(
            summary = "Generates finance report of specific user",
            description = """
                    Receives id of user for whom report is generated in request body.
                    Calls report service to obtain finance info and returns the DTO object with requested info.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleFinanceReportResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct"
                            //content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @GetMapping("/finances/{userId}")
    public ReportDto generateFinanceReport(@PathVariable String userId) { return service.generateFinanceReport(userId); }

    /**
     * REST method for getting user report.
     * @return ReportDto with number of all users in system
     */
    @Operation(
            summary = "Generates user report",
            description = """
                    Calls report service to obtain number of users and returns the DTO object with requested info.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleUserReportResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct"
//                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @GetMapping("/users")
    public ReportDto generateUserReport() { return service.generateUserReport(); }

    /**
     * REST method for getting book report of specific user.
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto with book report info
     */
    @Operation(
            summary = "Generates book report of specific user",
            description = """
                    Receives id of user for whom report is generated in request body.
                    Calls report service to obtain book info and returns the DTO object with requested info.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/SingleFinanceReportResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct"
//                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @GetMapping("/books/{userId}")
    public ReportDto generateBookReport(@PathVariable String userId) {
        return service.generateBookReport(userId);
    }
}
