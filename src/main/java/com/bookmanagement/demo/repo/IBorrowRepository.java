package com.bookmanagement.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookmanagement.demo.models.Borrow;


@Repository
public interface IBorrowRepository extends CrudRepository<Borrow, Integer>{
 
	
	@Query("SELECT b FROM Borrow b WHERE b.patron.id = :patronId AND b.book.id = :bookId AND returnDate IS NULL")
	Optional<Borrow> findByPatronIdAndBookId(int patronId , int bookId); 
	
	
}
