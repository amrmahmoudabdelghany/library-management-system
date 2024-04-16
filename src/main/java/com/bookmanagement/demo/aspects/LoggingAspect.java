package com.bookmanagement.demo.aspects;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component 
@Slf4j
public class LoggingAspect {

//	
//	 @Pointcut("within(@org.springframework.stereotype.Repository *)" +
//		        " || within(@org.springframework.stereotype.Service *)" +
//		        " || within(@org.springframework.web.bind.annotation.RestController *)")
//	private void bookServiceLogger() {} 
//	
	
	@Pointcut("within(com.bookmanagement.demo.service..*)")
	private void serviceLogger() {} 
	
	@Pointcut("execution(* com.bookmanagement.demo.service.IBookService.addBook(*))" +
			" || execution(* com.bookmanagement.demo.service.IBookService.updateBook(*))" + 
			"|| execution(* com.bookmanagement.demo.service.IPatronService.addPatron(*))" + 
			" || execution(* com.bookmanagement.demo.service.IPatronService.updatePatron(*))" + 
			" || execution(* com.bookmanagement.demo.service.IBorrowService.borrow(*))" + 
			" || execution(* com.bookmanagement.demo.service.IBorrowService.returnBook(*))")
	private void operationsPointcut() {} 
	
	
	@Around("operationsPointcut()")
	public Object logTime(ProceedingJoinPoint joinPoint)throws Throwable { 
	 
	   long startTime = System.currentTimeMillis() ; 
	   
		Object result = joinPoint.proceed() ; 
		
		if(log.isDebugEnabled()) { 
			
			log.debug("Method {}.{} take {} milli(s)" ,
					joinPoint.getSignature().getDeclaringTypeName() ,
					joinPoint.getSignature().getName() , 
					(System.currentTimeMillis() - startTime));
		}
		
		return result ; 
	}
	@Around("serviceLogger()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable { 
		 
		if(log.isDebugEnabled()) { 
		log.debug("Enter  {}.{} with argument(s) {}  " ,
				joinPoint.getSignature().getDeclaringTypeName()  , 
				joinPoint.getSignature().getName() , 
				Arrays.toString(joinPoint.getArgs())); 
		}
	  Object result;
	
	  result = joinPoint.proceed(); 
		if(log.isDebugEnabled()) { 
			log.debug("Exit  {}.{} with return {}  " ,
					joinPoint.getSignature().getDeclaringTypeName()  , 
					joinPoint.getSignature().getName() , 
					String.valueOf(result)); 
	
		}
	  return result ; 
		
	}
	
	@AfterThrowing(pointcut  = "serviceLogger()" , throwing = "th")
	public void logException(JoinPoint jp , Throwable th) {
		log.error("Exception in {}.{}() with cause = {} Where args was {} ",
				jp.getSignature().getDeclaringTypeName(),
	            jp.getSignature().getName(), th.getCause() != null ? th.getCause() : "NULL" , 
	            		Arrays.toString(jp.getArgs())) ;
	}
	
	
	
}
