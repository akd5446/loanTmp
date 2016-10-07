package com.i3.loan.controllers;

import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Book;
import com.i3.loan.repositories.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @Test
    public void canGetById() {
        // Given
        Book expected = new Book("foo", 5);
        given(bookRepository.findOne(123L)).willReturn(expected);

        // When
        Book actual = bookController.get(123L);

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void throwsNotFoundWhenRetrievingNonExistentBook() {
        // When
        bookController.get(123L);
    }

    @Test
    public void canGetListOfBooks() {
        // Given
        Book[] expected = {new Book("foo", 5), new Book("bar", 3), new Book("baz", 7)};
        given(bookRepository.findAll()).willReturn(Arrays.asList(expected));

        // When
        List<Book> actual = bookController.get();

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void getListEmptyIfNoBooksExist() {
        // When
        List<Book> actual = bookController.get();

        // Then
        assertThat(actual, is(empty()));
    }

    @Test
    public void canCreateNewBook() {
        // Given
        Book expected = new Book("foo", 5);
        given(bookRepository.save(expected)).willReturn(expected);

        // When
        Book actual = bookController.create(expected);

        // Then
        verify(bookRepository).save(expected);
        assertThat(actual, is(expected));
    }

    @Test
    public void canDeleteBook() {
        // When
        bookController.delete(123L);

        // Then
        verify(bookRepository).delete(123L);
    }

}