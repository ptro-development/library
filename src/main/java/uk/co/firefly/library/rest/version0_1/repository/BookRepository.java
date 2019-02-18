package uk.co.firefly.library.rest.version0_1.repository;

import uk.co.firefly.library.rest.version0_1.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
