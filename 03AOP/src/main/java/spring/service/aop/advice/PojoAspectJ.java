package spring.service.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;


public class PojoAspectJ {


    public void before(JoinPoint jp) {
        System.out.println("\n==========================================");
        System.out.println("[before/POJO] 호출 메서드 : " + jp.getSignature());
        Object[] args = jp.getArgs();
        if (args != null && args.length > 0) {
            System.out.println("[before/POJO] args[0] : " + args[0]);
        }
        System.out.println("==========================================");
    }

    public void afterReturning(JoinPoint jp, Object returnValue) {
        System.out.println("\n==========================================");
        System.out.println("[afterReturning/POJO] 호출 메서드 : " + jp.getSignature());
        System.out.println("[afterReturning/POJO] 반환값 : " + returnValue);
        System.out.println("==========================================");
    }

    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        System.out.println("\n==========================================");
        System.out.println("[around/POJO] 시작 : " + pjp.getSignature());
        try {
            Object ret = pjp.proceed();
            System.out.println("[around/POJO] 반환 : " + ret);
            return ret;
        } catch (Throwable t) {
            System.out.println("[around/POJO] 예외 : " + t.getClass().getName() + " - " + t.getMessage());
            throw t;
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[around/POJO] 종료 (소요 " + tookMs + " ms)");
            System.out.println("==========================================");
        }
    }

    public void afterThrowing(JoinPoint jp, Throwable throwable) {
        System.out.println("\n==========================================");
        System.out.println("[afterThrowing/POJO] 호출 메서드 : " + jp.getSignature());
        System.out.println("[afterThrowing/POJO] 예외 : " + throwable.getClass().getName()
                + " - " + throwable.getMessage());
        System.out.println("==========================================\n");
    }
}
