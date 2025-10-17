package spring.service.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * TestAspectJ02 (messageservice07.xml용, GET 전용)
 * - XML에서 <bean id="testAspect" class="spring.service.aop.advice.TestAspectJ02"/> 로 등록
 * - <aop:aspectj-autoproxy/> 필요 (07.xml에 있음)
 * - get*() 메서드에 대해서만 before/afterReturning/afterThrowing/around 적용
 * - @Component 미사용 (XML 등록)
 * - 네임드 @Pointcut 제거 → 각 어드바이스에 표현식 직접 기입
 */
@Aspect
public class TestAspectJ02 {

    // ===== Before : get*() 메서드만 매칭 =====
    @Before("execution(* *..get*(..))")
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

    // ===== After Returning : get*() 메서드만 매칭 =====
    @AfterReturning(pointcut = "execution(* *..get*(..))", returning = "returnValue")
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

    // ===== After Throwing : get*() 메서드만 매칭 =====
    @AfterThrowing(pointcut = "execution(* *..get*(..))", throwing = "ex")
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

    // ===== Around : get*() 메서드만 매칭 =====
    @Around("execution(* *..get*(..))")
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
