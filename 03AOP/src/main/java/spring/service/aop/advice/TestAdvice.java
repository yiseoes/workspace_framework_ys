package spring.service.aop.advice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class TestAdvice implements MethodBeforeAdvice,
                                    AfterReturningAdvice,
                                    ThrowsAdvice,
                                    MethodInterceptor {


    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("\n==========================================");
        System.out.println("[before 로그] " + getClass() + ".before() 시작.....");
        System.out.println("[before 로그] 호출된 타겟 객체의 메소드 :" + method);
        if (args != null && args.length != 0) {
            System.out.println("[before 로그] 타겟 객체 메소드에 전달된 인자 : " + args[0]);
        }
        System.out.println("[before 로그] " + getClass() + ".before() 종료.....");
    }


    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("\n==========================================");
        System.out.println("[afterReturning 로그] " + getClass() + ".afterReturning() 시작.....");
        System.out.println("[afterReturning 로그] 호출된 타겟 객체의 메소드 :" + method);
        if (args != null && args.length != 0) {
            System.out.println("[afterReturning 로그] 타겟 객체 메소드에 전달된 인자 : " + args[0]);
        }
        System.out.println("[afterReturning 로그] 메소드 반환 값 : " + returnValue);
        System.out.println("[afterReturning 로그] " + getClass() + ".afterReturning() 종료.....");
        System.out.println("==========================================\n");
    }


    public void afterThrowing(Method method, Object[] args, Object target, Throwable throwable) {
        System.out.println("\n==========================================");
        System.out.println("[throws 로그] " + getClass() + ".afterThrowing() 시작.....");
        System.out.println("[throws 로그] 호출된 타겟 객체의 메소드 :" + method);
        if (args != null && args.length != 0) {
            System.out.println("[throws 로그] 타겟 객체 메소드에 전달된 인자 : " + args[0]);
        }
        System.out.println("[throws 로그] 발생한 예외 : " + throwable.getClass().getName() + " - " + throwable.getMessage());
        System.out.println("[throws 로그] " + getClass() + ".afterThrowing() 종료.....");
        System.out.println("==========================================\n");
    }


    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.nanoTime();

        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        Object target = invocation.getThis();

        System.out.println("\n==========================================");
        System.out.println("[around 로그] " + getClass() + ".invoke() 시작.....");
        System.out.println("[around 로그] 호출된 타겟 객체의 메소드 :" + method);
        if (args != null && args.length != 0) {
            System.out.println("[around 로그] 타겟 객체 메소드에 전달된 인자 : " + args[0]);
        }

        try {
            Object ret = invocation.proceed();
            System.out.println("[around 로그] 메소드 반환 값 : " + ret);
            return ret;
        } catch (Throwable t) {
            System.out.println("[around 로그] 발생한 예외 : " + t.getClass().getName() + " - " + t.getMessage());
            throw t;
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[around 로그] " + getClass() + ".invoke() 종료..... (소요 시간: " + tookMs + " ms)");
            System.out.println("==========================================");
        }
    }
}