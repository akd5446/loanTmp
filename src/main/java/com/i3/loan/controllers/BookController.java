package com.i3.loan.controllers;

import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Book;
import com.i3.loan.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(path = "/{id}", method = GET)
    public Book get(@PathVariable long id) {
        Book book = bookRepository.findOne(id);

        if (book == null) {
            throw new NotFoundException(String.format("No book with ID: %s", id));
        }

        return book;
    }

    @RequestMapping(method = GET)
    public List<Book> get() {
        return bookRepository.findAll();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    public void delete(@PathVariable long id) {
        bookRepository.delete(id);
    }
}
