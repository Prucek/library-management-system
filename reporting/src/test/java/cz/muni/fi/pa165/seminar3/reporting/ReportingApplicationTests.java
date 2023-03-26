package cz.muni.fi.pa165.seminar3.reporting;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.reporting.service.BookReportDto;
import cz.muni.fi.pa165.seminar3.reporting.service.FinanceReportDto;
import cz.muni.fi.pa165.seminar3.reporting.service.ReportController;
import cz.muni.fi.pa165.seminar3.reporting.service.UserReportDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReportController.class)
class ReportingApplicationTests {

	// injected mock implementation of Spring MVC
	@Autowired
	private MockMvc mockMvc;

	// injected mock implementation of the ReportController
	@MockBean
	private ReportController reportController;

	@Test
	void contextLoads() {
	}

	@Test
	void getBookReport() throws Exception {
		String id = "is7sdf6a8ys4dhf78dfy";
		Instant instant = Instant.now();
		int borrowed = 6;
		int returned = 3;
		UserDto user = new UserDto();
		user.setId(id);
		user.setFirstName("James");
		user.setLastName("Bond");

		BookReportDto bookDto = new BookReportDto();
		bookDto.setGeneratedAt(instant);
		bookDto.setBorrowedBooksCount(borrowed);
		bookDto.setReturnedBooksCount(returned);
		bookDto.setUser(user);

		// define what mock service returns when called
		given(reportController.generateBookReport(id)).willReturn(bookDto);

		// call controller and check the result
		mockMvc.perform(get("/reports/books/" + id)
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.generatedAt").value(instant.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user").value(user))
				.andExpect(MockMvcResultMatchers.jsonPath("$.borrowedBooksCount").value(borrowed))
				.andExpect(MockMvcResultMatchers.jsonPath("$.returnedBooksCount").value(returned))
				.andDo(print());
	}

	@Test
	void getFinanceReport() throws Exception {
		String id = "is7sdf6a8ys4dhf78dfy";
		Instant instant = Instant.now();
		int finesTotal = 6;
		int finesPaid = 3;
		UserDto user = new UserDto();
		user.setId(id);
		user.setFirstName("James");
		user.setLastName("Bond");

		FinanceReportDto financeDto = new FinanceReportDto();
		financeDto.setGeneratedAt(instant);
		financeDto.setFinesCount(finesTotal);
		financeDto.setFinesTotalPaid(finesPaid);
		financeDto.setUser(user);

		// define what mock service returns when called
		given(reportController.generateFinanceReport(id)).willReturn(financeDto);

		// call controller and check the result
		mockMvc.perform(get("/reports/finances/" + id)
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.generatedAt").value(instant.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user").value(user))
				.andExpect(MockMvcResultMatchers.jsonPath("$.finesCount").value(finesTotal))
				.andExpect(MockMvcResultMatchers.jsonPath("$.finesTotalPaid").value(finesPaid))
				.andDo(print());
	}

	@Test
	void getUserReport() throws Exception {
		Instant instant = Instant.now();
		int usersCount = 6;
		int librarianCount = 3;
		int newUsersCount = 2;

		UserReportDto userDto = new UserReportDto();
		userDto.setGeneratedAt(instant);
		userDto.setUsersCount(usersCount);
		userDto.setLibrarianCount(librarianCount);
		userDto.setNewUserCount(newUsersCount);

		// define what mock service returns when called
		given(reportController.generateUserReport()).willReturn(userDto);

		// call controller and check the result
		mockMvc.perform(get("/reports/users")
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.generatedAt").value(instant.toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.usersCount").value(usersCount))
				.andExpect(MockMvcResultMatchers.jsonPath("$.librarianCount").value(librarianCount))
				.andExpect(MockMvcResultMatchers.jsonPath("$.newUserCount").value(newUsersCount))
				.andDo(print());
	}

	@Test
	void wrongURL() throws Exception {
		mockMvc.perform(get("/reports/wrong")
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void getNonexistentFinanceReport() throws Exception {

		given(reportController.generateFinanceReport("-1")).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"user with id=-1 not found"));

		mockMvc.perform(get("/reports/finances/-1")
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	void getNonexistentBookReport() throws Exception {
		given(reportController.generateBookReport("-1")).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"user with id=-1 not found"));

		mockMvc.perform(get("/reports/books/-1")
						.header("User-Agent", "007")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
	}


}
