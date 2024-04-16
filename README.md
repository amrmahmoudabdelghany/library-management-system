

 ## Library Management System 

 # Run : 
     - perform login using email "admin@library.org"  and password "123456" by GET /api/auth and get access token 
     - set retrived access token to Authorization header prefixed with Bearer 
     * all endpints needs to be authenticated excpect GET /api/auth


 Implemented Endpoints  
 
-Authentication 
	 - GET /api/auth : to perform authentication 
	   
	   - Access  public 

	   - request body  : 
		   {
		     email ,
		     password
		    } 
	    - response body : 
		    {
		      request , 
		      firstName , 
		      lastName , 
		      email , 
		      token 
		    }
		- constraint : 
			- email required 
			- password required	


	- POST /api/auth : to perform registeration 
	  
	   - Access ROLE_ADMIN  

	   - request body  : 
		   {
		     firstName ,
		     lastName , 
		     email , 
		     password , 
		     role 
		    } 
	    - response body : 
		    {
		      request , 
		      id , 
		      token  
		    }

		- constraint : 
			- all fields required expect role  
			- role default value is ROLE_LIBRARIAN
			- token valid 24-h
			- Only ROLE_ADMIN can register new account

-Book Management 

- GET /api/books : to retrive all books 

	   - Access ROLE_ADMIN , ROLE_LIBRARIAN

	   - request body  : 
		    blank 

	    - response body : 
		    {
		      books : { 
		      	[
		      		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed 
		      	]
		       }  
		    }



	 - GET /api/books/{id} : Retrieve details of a specific book by ID. 

    	   - Access ROLE_ADMIN , ROLE_LIBRARIAN

    	   - request body  : 
		        blank 

	        - response body : 
		      {
		      
		      		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed 
		      	 
		    }



	- POST /api/books : Add a new book to the library. 

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		    
		    {
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn ,  // format should be (isbn-10 or isbn-13)  
		      		isBorrowed  // default false 
		    } 

	      - response body : 
		    {
		      
		      		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed 
		      	 
		    }

		  - constraint : 
			- isbn should be unique 
			- all fields required expects isBorrowed 



	- PUT /api/books : Update an existing book's information. 

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		    
		    {
		     		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed   	 
		    } 

	      - response body : 
		    {
		      
		      		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed 
		      	 
		    }


		  - constraint : 
			- isbn should be unique 
			- all fields required expects isBorrowed 


	- DELETE /api/books/{id} : Remove a book from the library.

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		       blank 

	      - response body :  // The removed book 
		    {
		      
		      		id , 
		      		title , 
		      		author , 
		      		publicationYear , 
		      		isbn , 
		      		isBorrowed 
		      	 
		    }



- Patron Management endpoints : 

	 - GET /api/patrons : to retrive all patrons 

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		      blank 

	        - response body : 
		    {
		      patrons : { 
		      	[
		      		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone 
		      		 
		      	]
		       }  
		    }


	 - GET /api/patrons/{id} : Retrieve details of a specific patron by ID. 

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		    blank 

	        - response body : 
		        {
		
		      		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone 
		      	 
		    }


	- POST /api/patrons : Add a new patron to the system.

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		    
		        {
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone 	 
		        } 

	      - response body : 
		      {
		      
		      		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone 
		      } 
		  - constraint : 
			- all fields reuired 
			- email and phone must be unique



	- PUT /api/patrons : Update an existing patron's information.

	   - Access ROLE_ADMIN , ROLE_LIBRARIAN

	         - request body  : 
		    
		        {
		     		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone  	 
		        } 

	          - response body : 
		        {
		      
		      		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone  
		        }

	        	- constraint : 
	      		- all fields reuired 
	      		- email and phone must be unique

	- DELETE /api/patrons/{id} : Remove a patron from the system.

	       - Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		       blank 

	      - response body :  // The removed patron 
		    {
		      
		      		id , 
		      		firstName , 
		      		lastName , 
		      		email , 
		      		phone 
		      	 
		    }

- Borrowing endpoints : 
		
	- POST /api/{bookId}/patron/{patronId} : Allow a patron to borrow a book.
	
	    	- Access ROLE_ADMIN , ROLE_LIBRARIAN

	       - request body  : 
		        blank 

	        - response body : 
		        {
		     
		      		id , 
		      		patronId , 
		      		bookId , 
		      		patronName , 
		      		bookTitle
		    
		        }

	    	- constraint : 
		  	- book should not be borrowed before
			

	- PUT /api/{bookId}/patron/{patronId} : Record the return of a borrowed book by a patron.
	
		- Access ROLE_ADMIN , ROLE_LIBRARIAN

      	       - request body  : 
      		    blank 
      
      	      - response body : 
      		      {
		     
		         		request , 
		      		id , 
		      		patronId , 
		      		bookId , 
		      		patronName , 
		      		bookTitle
		 
		    }
        
        		- constraint : 
        			- book should be already borrowed by the referenced patron 
        
