package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    // Ideally, the path to the service should be obtained from a configuration file
    public final WebClient client = WebClient.create("http://localhost:8080/");

    /**
     * Generates report with number of fines and total paid fines of specific user.
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto object with number of fines and total paid fines of specific user
     */
    public ReportDto generateFinanceReport(String userId) {
        // TODO implement logic for for getting real values, when fine dto and service will be created
        // TODO uncomment when all four major services can communicate among them
        // So far use mock values

        ReportDto report = new ReportDto();
//        UserDto user = client.get()
//                .uri(uriBuilder -> uriBuilder.pathSegment("users", userId).build())
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .block();

        report.setGeneratedAt(Instant.now());
        //report.setUser(user);
        report.setFinesCount(5);
        report.setFinesTotalPaid(100);

        return report;
    }

    /**
     * Generate report with number of users in system.
     * @return ReportDto object with set number of users in system
     */
    public ReportDto generateUserReport() {
        // TODO uncomment when all four major services can communicate among them
        // So far use mock values

        ReportDto report = new ReportDto();

//        List<UserDto> users = new ArrayList<>();
//        Result<UserDto> usersPage;
//        int page = 0;

//        while (true) {
//            int finalPage = page;
//
//            usersPage = client
//                    .get()
//                    .uri(uriBuilder -> uriBuilder.path("/users").queryParam("page", finalPage).build())
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<Result<UserDto>>() {})
//                    .block();
//
//            if (noMoreUsers(usersPage)) {
//                break;
//            }
//
//            users.addAll(usersPage.getItems());
//            page++;
//        }

        report.setGeneratedAt(Instant.now());
        // report.setUsersCount(users.size());
        report.setUsersCount(10);

        return report;
    }

    /**
     * Generates report with number of borrowed and returned books of specific user.
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto object with number of borrowed and returned books of specific user
     */
    public ReportDto generateBookReport(String userId) {
        // TODO uncomment when all four major services can communicate among them
        // So far use mock values

        ReportDto report = new ReportDto();

//        UserDto user = client.get()
//                .uri(uriBuilder -> uriBuilder.pathSegment("users", userId).build())
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .block();
//
//        List<BorrowingDto> borrowings = getUserBorrowings(userId);
//        int returnedBookCounter = 0, borrowedBookCounter = 0;
//
//        for (var borrowing : borrowings) {
//            if (borrowing.getTo() != null) {
//                returnedBookCounter++;
//            }
//            borrowedBookCounter++;
//        }

        report.setGeneratedAt(Instant.now());
        // report.setUser(user);
        // report.setBorrowedBooksCount(borrowedBookCounter);
        // report.setReturnedBooksCount(returnedBookCounter);
        report.setBorrowedBooksCount(1);
        report.setReturnedBooksCount(1);

        return report;
    }

    /**
     * Returns all borrowings of specific user.
     * @param userId Specifies user for whom the borrowings are returned
     * @return List of borrowings for specific user
     */
    private List<BorrowingDto> getUserBorrowings(String userId) {
        List<BorrowingDto> borrowings = new ArrayList<>();
        Result<BorrowingDto> borrowingsPage;
        int page = 0;

        while (true) {
            int finalPage = page;

            borrowingsPage = client
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/borrowings").queryParam("page", finalPage).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Result<BorrowingDto>>() {})
                    .block();

            if (noMoreBorrowings(borrowingsPage)) {
                break;
            }

            borrowings.addAll(borrowingsPage.getItems()
                    .stream()
                    .filter(r -> r.getUser().getId().equals(userId))
                    .collect(Collectors.toList()));

            page++;
        }

        return borrowings;
    }

    /**
     * Checks if there left some user on specific page
     * @param usersPage Actual page which is checked
     * @return Flag which represent whether there left some more users
    */
    private boolean noMoreUsers(Result<UserDto> usersPage) {
        return usersPage == null || usersPage.getItems() == null || usersPage.getItems().isEmpty();
    }

    /**
     * Checks if there left some borrowings on specific page
     * @param borrowingsPage Actual page which is checked
     * @return Flag which represent whether there left some more borrowings
     */
    private boolean noMoreBorrowings(Result<BorrowingDto> borrowingsPage) {
        return borrowingsPage == null || borrowingsPage.getItems() == null || borrowingsPage.getItems().isEmpty();
    }
}
