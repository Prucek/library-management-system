package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing book create data.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateDto {

    private String title;

    private List<String> authorIds;
}
