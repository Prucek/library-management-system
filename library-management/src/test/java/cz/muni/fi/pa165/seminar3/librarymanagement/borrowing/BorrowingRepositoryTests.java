package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for the borrowing repository.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LibraryManagementApplication.class)
@Transactional
public class BorrowingRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private BorrowingRepository borrowingRepository;

    private Borrowing returned;

    private Borrowing notReturned;

    private final Faker faker = new Faker();

    /**
     * Setups entities for tests.
     */
    @BeforeEach
    public void beforeEach() {
        returned = fakeBorrowing(faker);
        returned.setReturned(LocalDateTime.now());
        returned.setBookInstance(em.merge(returned.getBookInstance()));
        returned.setUser(em.merge(returned.getUser()));
        returned = em.merge(returned);

        notReturned = fakeBorrowing(faker);
        notReturned.setReturned(null);
        notReturned.setBookInstance(em.merge(notReturned.getBookInstance()));
        notReturned.setUser(em.merge(notReturned.getUser()));
        notReturned = em.merge(notReturned);
    }

    @Test
    public void findReturned() {
        List<Borrowing> result = borrowingRepository.findPendingOfBookInstance(returned.getBookInstance().getId());

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void findNotReturned() {
        List<Borrowing> result = borrowingRepository.findPendingOfBookInstance(notReturned.getBookInstance().getId());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(notReturned.getId());
    }

    /**
     * Clears the database.
     */
    @AfterEach
    public void afterEach() {
        em.remove(returned.getBookInstance());
        em.remove(returned.getUser());
        em.remove(returned);

        em.remove(notReturned.getBookInstance());
        em.remove(notReturned.getUser());
        em.remove(notReturned);
    }
}
