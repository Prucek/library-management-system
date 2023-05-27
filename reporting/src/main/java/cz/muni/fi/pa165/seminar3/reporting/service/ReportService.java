package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.BookReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.FinanceReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.UserReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.reporting.LibraryManagementApi;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents report service which implements reports of users, books and fines.
 *
 * @author Marek Miček
 */
@Service
public class ReportService {

    private final LibraryManagementApi libraryManagementApi;

    /**
     * Creates a new Report service instance.
     *
     * @param libraryManagementApi Library management API client instance
     */
    @Autowired
    public ReportService(LibraryManagementApi libraryManagementApi) {
        this.libraryManagementApi = libraryManagementApi;
    }

    /**
     * Generates report with number of borrowed and returned books of specific user.
     *
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto object with number of borrowed and returned books of specific user
     */
    public BookReportDto generateBookReport(String userId) {
        BookReportDto report = new BookReportDto();

        UserDto user = libraryManagementApi.getUserDto(userId);

        List<BorrowingDto> borrowings = getBorrowings();
        int returnedBookCounter = 0;
        int borrowedBookCounter = 0;

        for (var borrowing : borrowings) {
            if (borrowing.getBorrowedTo() != null) {
                returnedBookCounter++;
            }
            borrowedBookCounter++;
        }

        report.setGeneratedAt(Instant.now());
        report.setUser(user);
        report.setBorrowedBooksCount(borrowedBookCounter);
        report.setReturnedBooksCount(returnedBookCounter);

        return report;
    }

    /**
     * Generates report with number of fines and total paid fines.
     *
     * @param userId Specifies user for whom the report is generated
     * @return ReportDto object with number of fines and total paid fines
     */
    public FinanceReportDto generateFinanceReport(String userId) {
        FinanceReportDto report = new FinanceReportDto();
        UserDto user = libraryManagementApi.getUserDto(userId);

        List<FineDto> fines = getFines();
        double totalPaid = 0.0;

        for (var fine : fines) {
            totalPaid += fine.getAmount();
        }

        report.setGeneratedAt(Instant.now());
        report.setUser(user);
        report.setFinesCount(fines.size());
        report.setFinesTotalPaid(totalPaid);

        return report;
    }

    /**
     * Generate report with number of users in system.
     *
     * @return ReportDto object with set number of users in system
     */
    public UserReportDto generateUserReport() {
        UserReportDto report = new UserReportDto();

        List<UserDto> users = getAllUsers();

        report.setGeneratedAt(Instant.now());
        report.setUsersCount(getUsersCount(users));
        report.setLibrarianCount(users.size() - report.getUsersCount());
        report.setNewUserCount(getNewUsersCount(users));

        return report;
    }

    /**
     * Returns all users in system.
     *
     * @return List of all users in system
     */
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        Result<UserDto> usersPage;
        int page = 0;

        while (true) {
            int finalPage = page;

            usersPage = libraryManagementApi.getUsers(finalPage);

            if (noMoreUsers(usersPage)) {
                break;
            }

            users.addAll(usersPage.getItems());
            page++;
        }
        return users;
    }

    /**
     * Returns all fines.
     *
     * @return List of fines
     */
    public List<FineDto> getFines() {
        List<FineDto> fines = new ArrayList<>();
        Result<FineDto> finesPage;
        int page = 0;

        while (true) {
            int finalPage = page;

            finesPage = libraryManagementApi.getFines(finalPage);

            if (noMoreFines(finesPage)) {
                break;
            }

            fines.addAll(finesPage.getItems());

            page++;
        }

        return fines;
    }

    /**
     * Returns all borrowings.
     *
     * @return List of borrowings
     */
    public List<BorrowingDto> getBorrowings() {
        List<BorrowingDto> borrowings = new ArrayList<>();
        Result<BorrowingDto> borrowingsPage;
        int page = 0;

        while (true) {
            int finalPage = page;

            borrowingsPage = libraryManagementApi.getBorrowings(finalPage);

            if (noMoreBorrowings(borrowingsPage)) {
                break;
            }

            borrowings.addAll(borrowingsPage.getItems());

            page++;
        }

        return borrowings;
    }

    /**
     * Checks if there left some fines on specific page.
     *
     * @param finesPage Actual page which is checked
     * @return Flag which represent whether there left some more fines
     */
    private boolean noMoreFines(Result<FineDto> finesPage) {
        return finesPage == null || finesPage.getItems() == null || finesPage.getItems().isEmpty();
    }

    /**
     * Checks if there left some users on specific page.
     *
     * @param usersPage Actual page which is checked
     * @return Flag which represent whether there left some more users
     */
    private boolean noMoreUsers(Result<UserDto> usersPage) {
        return usersPage == null || usersPage.getItems() == null || usersPage.getItems().isEmpty();
    }

    /**
     * Checks if there left some borrowings on specific page.
     *
     * @param borrowingsPage Actual page which is checked
     * @return Flag which represent whether there left some more borrowings
     */
    private boolean noMoreBorrowings(Result<BorrowingDto> borrowingsPage) {
        return borrowingsPage == null || borrowingsPage.getItems() == null || borrowingsPage.getItems().isEmpty();
    }

    /**
     * Counts number of users (clients not librarians) in system.
     *
     * @param users List of all users in system
     * @return Count of clients in system
     */
    private int getUsersCount(List<UserDto> users) {
        int userCounter = 0;
        for (var user : users) {
            if (user.getUserType() == UserType.CLIENT) {
                userCounter++;
            }
        }
        return userCounter;
    }

    /**
     * Counts number of new users in system (account is not older than 3 days).
     *
     * @param users List of all users in system
     * @return Count of new users in system
     */
    private int getNewUsersCount(List<UserDto> users) {
        int newUserCounter = 0;
        for (var user : users) {
            if (user.getCreatedAt().isAfter(LocalDateTime.now().minusDays(3))) {
                newUserCounter++;
            }
        }
        return newUserCounter;
    }
}
