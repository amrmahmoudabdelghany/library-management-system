package com.bookmanagement.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.exceptions.AlreadyExistException;
import com.bookmanagement.demo.models.exceptions.AppException;
import com.bookmanagement.demo.models.exceptions.NotFoundException;
import com.bookmanagement.demo.models.exceptions.ValidationException;
import com.bookmanagement.demo.service.BookService;
import com.bookmanagement.demo.service.IBookService;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books") 
@PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN')")
public class BookController {

	private final IBookService bookService;

	@GetMapping
	public ResponseEntity<Map<String, List<Book>>> getAllBook() {
		return ResponseEntity.ok(Map.of("books", this.bookService.findAllBooks()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Integer id) {

		return ResponseEntity.ok( this.bookService.retriveBookById(id));
	}

	@PostMapping
	public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
		Book newBook = this.bookService.addBook(book);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newBook.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Book> updateBook(@RequestBody Book book) {
		return ResponseEntity.ok(this.bookService.updateBook(book));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Book> removeBook(@PathVariable Integer id) {
		return ResponseEntity.ok(this.bookService.removeBook(id));
	}

}
