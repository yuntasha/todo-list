package practice.project.todo_list.global.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionTimeLogger {
    @Pointcut("@annotation(LogExecutionTime)")
    public void logExecutionTimePointcut() {}

    @Around("logExecutionTimePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long excutionTime = System.currentTimeMillis() - start;

        System.out.printf("[%s] : %d\n", joinPoint.getSignature(), excutionTime);

        return result;
    }
}
