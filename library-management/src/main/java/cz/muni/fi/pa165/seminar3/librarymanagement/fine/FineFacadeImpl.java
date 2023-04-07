package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing fine facade.
 *
 * @author Juraj Marcin
 */
@Service
public class FineFacadeImpl extends DomainFacadeImpl<Fine, FineDto, FineCreateDto> implements FineFacade {

    @Getter
    private final FineService domainService;
    @Getter
    private final FineMapper domainMapper;
    private final UserService userService;
    private final BorrowingService borrowingService;

    /**
     * Creates a new fine facade instance.
     *
     * @param domainService    fine service instance
     * @param domainMapper     fine mapper instance
     * @param userService      user service instance
     * @param borrowingService borrowing service instance
     */
    @Autowired
    public FineFacadeImpl(FineService domainService, FineMapper domainMapper, UserService userService,
                          BorrowingService borrowingService) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.userService = userService;
        this.borrowingService = borrowingService;
    }

    @Override
    public FineDto create(FineCreateDto fineCreateDto) {
        User issuer = userService.find(fineCreateDto.getIssuerId());
        Borrowing outstandingBorrowing = borrowingService.find(fineCreateDto.getOutstandingBorrowingId());
        Fine fine = Fine.builder()
                .amount(fineCreateDto.getAmount())
                .outstandingBorrowing(outstandingBorrowing)
                .issuer(issuer)
                .build();
        return domainMapper.toDto(domainService.create(fine));
    }

    @Override
    public FineDto updateFine(String id, FineCreateDto fineCreateDto) {
        Fine fine = domainService.find(id);
        User issuer = userService.find(fineCreateDto.getIssuerId());
        Borrowing outstandingBorrowing = borrowingService.find(fineCreateDto.getOutstandingBorrowingId());
        fine.setAmount(fineCreateDto.getAmount());
        fine.setIssuer(issuer);
        fine.setOutstandingBorrowing(outstandingBorrowing);
        return domainMapper.toDto(domainService.update(fine));
    }

    @Override
    public void deleteFine(String fineId) {
        Fine fine = domainService.find(fineId);
        domainService.delete(fine);
    }
}
