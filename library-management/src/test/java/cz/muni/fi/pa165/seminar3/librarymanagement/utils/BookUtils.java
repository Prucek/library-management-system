package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthor;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthorDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;

/**
 * Class containing Book test utility methods.
 *
 * @author Marek Fiala
 */
public class BookUtils {

    public static Book fakeBook(Faker faker){
        return Book.builder()
                .id(faker.internet().uuid())
                .title(faker.book().title())
                .author(fakeAuthor(faker))
                .instance(BookInstance.builder().pages(faker.number().numberBetween(50, 200)).build())
                .build();
    }

    public static BookDto fakeBookDto(Faker faker){
        return BookDto.builder()
                .id(faker.internet().uuid())
                .title(faker.book().title())
                .author(fakeAuthorDto(faker))
                .instance(BookInstanceDto.builder().pages(faker.number().numberBetween(50, 200)).build())
                .build();
    }
}
