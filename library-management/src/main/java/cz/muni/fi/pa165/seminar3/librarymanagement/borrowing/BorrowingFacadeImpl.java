package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing borrowing facade.
 *
 * @author Marek Miček
 */
@Service
public class BorrowingFacadeImpl extends DomainFacadeImpl<Borrowing, BorrowingDto, BorrowingCreateDto>
        implements BorrowingFacade {

    @Getter
    private final BorrowingService domainService;
    @Getter
    private final BorrowingMapper domainMapper;
    private final UserService userService;
    private final BookService bookService;

    /**
     * Creates a new borrowing facade instance.
     *
     * @param domainService borrowing service instance
     * @param domainMapper  borrowing mapper instance
     * @param userService   user service instance
     * @param bookService   book service instance
     */
    @Autowired
    public BorrowingFacadeImpl(BorrowingService domainService, BorrowingMapper domainMapper, UserService userService,
                               BookService bookService) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.userService = userService;
        this.bookService = bookService;
    }

    /**
     * Updates existing borrowing.
     *
     * @param id                 id of borrowing to update
     * @param borrowingCreateDto new borrowing values
     * @return update borrowing DTO
     */
    @Override
    public BorrowingDto updateBorrowing(String id, BorrowingCreateDto borrowingCreateDto) {
        Borrowing borrowing = domainService.find(id);
        User user = userService.find(borrowingCreateDto.getUserId());
        BookInstance bookInstance = bookService.getInstance(borrowingCreateDto.getBookInstanceId());

        borrowing.setBorrowedTo(borrowingCreateDto.getBorrowedTo());
        borrowing.setBorrowedFrom(borrowingCreateDto.getBorrowedFrom());
        borrowing.setReturned(borrowingCreateDto.getReturned());
        borrowing.setUser(user);
        borrowing.setBookInstance(bookInstance);

        return domainMapper.toDto(domainService.update(borrowing));
    }

    /**
     * Deletes borrowing.
     *
     * @param borrowingId id to delete
     */
    @Override
    public void deleteBorrowing(String borrowingId) {
        Borrowing borrowing = domainService.find(borrowingId);
        domainService.delete(borrowing);
    }

    /**
     * Creates borrowing.
     *
     * @param borrowingCreateDto dto to create from
     * @return created borrowing DTO
     */
    @Override
    public BorrowingDto create(BorrowingCreateDto borrowingCreateDto) {
        User user = userService.find(borrowingCreateDto.getUserId());
        BookInstance bookInstance = bookService.getInstance(borrowingCreateDto.getBookInstanceId());

        Borrowing borrowing = Borrowing.builder()
                .borrowedTo(borrowingCreateDto.getBorrowedTo())
                .borrowedFrom(borrowingCreateDto.getBorrowedFrom())
                .returned(borrowingCreateDto.getReturned())
                .user(user)
                .bookInstance(bookInstance)
                .build();

        return domainMapper.toDto(domainService.create(borrowing));
    }

    @Override
    public List<BorrowingDto> findPending(String bookInstanceId) {
        return domainMapper.toDtoList(domainService.findPending(bookInstanceId));
    }
}
