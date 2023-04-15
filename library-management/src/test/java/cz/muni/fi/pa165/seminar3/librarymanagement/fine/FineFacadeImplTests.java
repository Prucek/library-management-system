package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class FineFacadeImplTests {

    @Autowired
    private FineRepository domainRepository;

    @Autowired
    private FineService domainService;

    @Autowired
    private FineMapper domainMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BorrowingService borrowingService;

    @Autowired
    private FineFacadeImpl fineFacade;

    private final Faker faker = new Faker();

    @Test
    public void createFineSuccessful() {

        FineCreateDto fineCreateDto =
                FineCreateDto.builder().amount(10.).issuerId("random").outstandingBorrowingId("random").build();
        Borrowing fakeBorrowing = fakeBorrowing(faker);
        User fakeUser = fakeBorrowing.getUser();
        Fine fakeFine = Fine.builder().amount(10.).issuer(fakeUser).outstandingBorrowing(fakeBorrowing).build();

        given(userService.find(any(String.class))).willReturn(fakeUser);
        given(borrowingService.find(any(String.class))).willReturn(fakeBorrowing);

        FineDto result = fineFacade.create(fineCreateDto);
        assertThat(domainMapper.fromDto(result)).isEqualTo(fakeFine);
        assertThat(domainRepository.findById(result.getId())).isPresent();
    }

    @Test
    public void createFineEmptyCreateDto() {

        FineCreateDto fineCreateDto = FineCreateDto.builder().build();
        Fine fakeFine = Fine.builder().build();

        given(userService.find(any(String.class))).willReturn(null);
        given(borrowingService.find(any(String.class))).willReturn(null);

        FineDto result = fineFacade.create(fineCreateDto);
        assertThat(domainMapper.fromDto(result)).isEqualTo(fakeFine);
        assertThat(domainRepository.findById(result.getId())).isPresent();
    }

    @Test
    public void createFineNullCreateDto() {

        FineCreateDto fineCreateDto = null;

        assertThrows(NullPointerException.class, () -> fineFacade.create(fineCreateDto));
    }
}
