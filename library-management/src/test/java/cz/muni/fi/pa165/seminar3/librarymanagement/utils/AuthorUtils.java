package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import java.util.Collections;

/**
 * Class containing Author test utility methods.
 *
 * @author Marek Fiala
 */
public class AuthorUtils {


    /**
     * Generates a fake author entity.
     *
     * @param faker faker instance
     * @return author entity
     */
    public static Author fakeAuthor(Faker faker) {
        return Author.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .surname(faker.name().lastName())
                .publications(Collections.emptyList())
                .build();
    }

    /**
     * Generates a fake authorDto entity.
     *
     * @param faker faker instance
     * @return authorDto entity
     */
    public static AuthorDto fakeAuthorDto(Faker faker) {
        return AuthorDto.builder()
                .id(faker.internet().uuid())
                .name(faker.name().firstName())
                .surname(faker.name().lastName())
                .build();
    }
}
