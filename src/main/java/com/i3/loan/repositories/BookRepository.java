package com.i3.loan.repositories;

import com.i3.loan.models.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAll();
}
