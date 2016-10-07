package com.i3.loan.controllers;

import com.i3.loan.models.Book;
import com.i3.loan.repositories.BookRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BookRepository bookRepository;

    @After
    public void cleanup() {
        bookRepository.deleteAll();
    }

    @Test
    public void getBookByIdReturnsNotFoundIfDoesNotExist()
            throws Exception {
        // When
        ResponseEntity actual = testRestTemplate.getForEntity("/books/1", Book.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getBookById()
            throws Exception {
        // Given
        Book expected = new Book("foo", 5);
        expected = bookRepository.save(expected);

        // When
        Long id = expected.getId();
        Book actual = testRestTemplate.getForObject("/books/" + id, Book.class);

        // Then
        assertThat(expected, is(actual));
    }

    @Test
    public void getAllBooks()
        throws Exception {
        // Given
        Book[] expected = {new Book("foo", 5), new Book("bar", 3), new Book("baz", 7)};
        bookRepository.save(Arrays.asList(expected));

        // When
        List<Book> actual = Arrays.asList(testRestTemplate.getForObject("/books", Book[].class));

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void createNewBook()
        throws Exception {
        // Given
        Book expected = new Book("foo", 5);
        assertThat(bookRepository.count(), is(0L));

        // When
        Book actual = testRestTemplate.postForObject("/books", expected, Book.class);

        // Then
        assertThat(expected.getTitle(), is(actual.getTitle()));
        assertThat(expected.getCopies(), is(actual.getCopies()));

        assertThat(bookRepository.count(), is(1L));
        assertThat(bookRepository.findOne(actual.getId()), is(actual));
    }

    @Test
    public void deleteBook()
        throws Exception {
        // Given
        Book book = bookRepository.save(new Book("foo", 5));
        Long id = book.getId();

        // When
        testRestTemplate.delete("/books/" + id);

        // Then
        assertThat(bookRepository.exists(id), is(false));
    }

    @Test
    public void deleteNonExistentBookDoesNotReturnError()
        throws Exception {
        // When
        testRestTemplate.delete("/books/1");
    }
}
