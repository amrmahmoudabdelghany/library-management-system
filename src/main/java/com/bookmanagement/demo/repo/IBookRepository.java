package com.bookmanagement.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookmanagement.demo.models.Book;



@Repository
public interface IBookRepository extends CrudRepository<Book,Integer>{
 
	boolean existsByISBN(String ISBN) ;  
	Book  findBookByISBN(String ISBN) ; 
}
