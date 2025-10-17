package spring.service.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


/**
 * TestAspectJ01 (messageservice06.xml용, GET 전용)
 * - XML에서 <bean id="testAspect" class="spring.service.aop.advice.TestAspectJ01"/> 로 직접 등록
 * - <aop:aspectj-autoproxy/> 필요 (06.xml에 이미 있음)
 * - getMessage(..) 에 대해서만 before/afterReturning/afterThrowing/around 적용
 * - @Component 미사용 (XML로 등록하기 때문에 중복 방지)
 * - 포인트컷으로 work() 메서드 콜(getMessage(..) 실행 지점)
 */

@Aspect
public class TestAspectJ01 {

    // work 포인트컷: spring.. 하위 패키지의 getMessage(..) 실행 지점 매칭
    //  - Pointcut 메서드는 "이름표" 역할이므로 반드시 빈 몸체(아무 처리 X)여야 함
    @Pointcut("execution(* spring..*.getMessage(..))")
    public void work() { 

    }

    // ===== Before =====
    @Before("work()")
    public void before(JoinPoint jp) {
        System.out.println("\n==========================================");
        System.out.println("[before 로그] " + getClass().getSimpleName() + ".before() 시작.....");
        System.out.println("[before 로그] targetObject call Method : " + jp.getSignature());
        Object[] args = jp.getArgs();
        if (args != null && args.length > 0) {
            System.out.println("[before 로그] targetObject method 전달 argument : " + args[0]);
        }
        System.out.println("[before 로그] " + getClass().getSimpleName() + ".before() 종료.....");
    }

    // ===== After Returning =====
    @AfterReturning(pointcut="work()", returning="returnValue")
    public void afterReturning(JoinPoint jp, Object returnValue) {
        System.out.println("\n==========================================");
        System.out.println("[afterReturning 로그] " + getClass().getSimpleName() + ".afterReturning() 시작.....");
        System.out.println("[afterReturning 로그] targetObject call Method : " + jp.getSignature());
        Object[] args = jp.getArgs();
        if (args != null && args.length > 0) {
            System.out.println("[afterReturning 로그] targetObject method 전달 argument : " + args[0]);
        }
        System.out.println("[afterReturning 로그] method returnValue : " + returnValue);
        System.out.println("[afterReturning 로그] " + getClass().getSimpleName() + ".afterReturning() 종료.....");
        System.out.println("==========================================\n");
    }

    // ===== After Throwing =====
    @AfterThrowing(pointcut="work()", throwing="ex")
    public void afterThrowing(JoinPoint jp, Throwable ex) {
        System.out.println("\n==========================================");
        System.out.println("[throws 로그] " + getClass().getSimpleName() + ".afterThrowing() 시작.....");
        System.out.println("[throws 로그] targetObject call Method : " + jp.getSignature());
        Object[] args = jp.getArgs();
        if (args != null && args.length > 0) {
            System.out.println("[throws 로그] targetObject method 전달 argument : " + args[0]);
        }
        System.out.println("[throws 로그] throw : " + ex.getClass().getName() + " - " + ex.getMessage());
        System.out.println("[throws 로그] " + getClass().getSimpleName() + ".afterThrowing() 종료.....");
        System.out.println("==========================================\n");
    }

    // ===== Around =====
    @Around("work()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        System.out.println("\n==========================================");
        System.out.println("[around 로그] " + getClass().getSimpleName() + ".around() 시작.....");
        System.out.println("[around 로그] targetObject call Method : " + pjp.getSignature());
        Object[] args = pjp.getArgs();
        if (args != null && args.length > 0) {
            System.out.println("[around 로그] targetObject method 전달 argument : " + args[0]);
        }
        try {
            Object ret = pjp.proceed();
            System.out.println("[around 로그] 메소드 반환 값 : " + ret);
            return ret;
        } catch (Throwable t) {
            System.out.println("[around 로그] 발생한 예외 : " + t.getClass().getName() + " - " + t.getMessage());
            throw t;
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[around 로그] " + getClass().getSimpleName() + ".around() 종료..... (소요 시간: " + tookMs + " ms)");
            System.out.println("==========================================");
        }
    }
}
