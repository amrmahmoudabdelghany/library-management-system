package com.bookmanagement.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bookmanagement.demo.models.Book;
import com.bookmanagement.demo.models.Patron;
import com.bookmanagement.demo.service.IPatronService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/patrons")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN')")
public class PatronController {

	private final IPatronService patronService ; 
	
	
  @GetMapping
  public ResponseEntity<Map<String, List<Patron>>> retriveAllPatrons() {
	  return ResponseEntity.ok(Map.of("patrons", this.patronService.findAllPatrons()));
	}
  

	@GetMapping("/{id}")
	public ResponseEntity<Patron> getPatronById(@PathVariable Integer id) {

		return ResponseEntity.ok( this.patronService.retrivePatronById(id));
	}

	@PostMapping
	public ResponseEntity<Patron> addNewPatron(@RequestBody Patron patron) {
		Patron newPatron = this.patronService.addPatron(patron);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newPatron.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Patron> updatePatron(@RequestBody Patron patron) {
		return ResponseEntity.ok(this.patronService.updatePatron(patron));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Patron> removePatron(@PathVariable Integer id) {
		return ResponseEntity.ok(this.patronService.removePatron(id));
	}

	
}
