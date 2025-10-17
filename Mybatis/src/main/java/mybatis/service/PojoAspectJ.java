package mybatis.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

public class PojoAspectJ {

	public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
	    System.out.println("AOP Around 시작");
	    System.out.println("Target Class : " + joinPoint.getTarget().getClass().getName());
	    System.out.println("Method       : " + joinPoint.getSignature());
	    System.out.println("Args Count   : " + joinPoint.getArgs().length);

	    Object[] args = joinPoint.getArgs();
	    for (int i = 0; i < args.length; i++) {
	        System.out.println("Arg[" + i + "] : " + args[i]);
	    }

	    Object result = null;
	    try {
	        result = joinPoint.proceed();
	        System.out.println("Return Value : " + result);
	    } catch (Throwable t) {
	        System.out.println("Exception    : " + t);
	        throw t;
	    } finally {
	        System.out.println("AOP Around 종료");
	    }

	    return result;

	}
}



//@Aspect
//public class PojoAspectJ {
//
//    @Around("execution(* mybatis.service.user..*Impl.*(..))")
//    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 동적으로 타겟 클래스 가져오기
//        Class<?> targetClass = joinPoint.getTarget().getClass();
//        
//        // 타겟 클래스 정보 로그
//        System.out.println("Invoking method on class: " + targetClass.getName());
//
//        // 원래 메서드 실행
//        return joinPoint.proceed();
//    }
//}
