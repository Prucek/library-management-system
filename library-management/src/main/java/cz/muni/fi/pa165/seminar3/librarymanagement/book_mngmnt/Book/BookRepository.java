package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
