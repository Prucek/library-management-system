package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthor;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthorDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class containing Book test utility methods.
 *
 * @author Marek Fiala
 */
public class BookUtils {

    /**
     * Generates a fake book instance entity.
     *
     * @param faker faker instance
     * @return fake book instance entity
     */
    public static BookInstance fakeBookInstance(Faker faker) {
        return BookInstance.builder().id(faker.internet().uuid()).borrowings(Collections.emptyList()).build();
    }

    /**
     * Generates a fake book instance dto.
     *
     * @param faker faker instance
     * @return fake book instance dto
     */
    public static BookInstanceDto fakeBookInstanceDto(Faker faker) {
        return BookInstanceDto.builder().id(faker.internet().uuid()).build();
    }

    /**
     * Generates a fake book entity.
     *
     * @param faker faker instance
     * @return fake book entity
     */
    public static Book fakeBook(Faker faker) {
        return Book.builder()
                .id(faker.internet().uuid())
                .title(faker.book().title())
                .authors(new ArrayList<>(List.of(fakeAuthor(faker))))
                .instances(new ArrayList<>(List.of(fakeBookInstance(faker))))
                .reservations(Collections.emptyList())
                .build();
    }

    /**
     * Generates a fake book dto.
     *
     * @param faker faker instance
     * @return fake book dto
     */
    public static BookDto fakeBookDto(Faker faker) {
        return BookDto.builder()
                .id(faker.internet().uuid())
                .title(faker.book().title())
                .authors(new ArrayList<>(List.of(fakeAuthorDto(faker))))
                .instances(new ArrayList<>(List.of(fakeBookInstanceDto(faker))))
                .build();
    }
}
