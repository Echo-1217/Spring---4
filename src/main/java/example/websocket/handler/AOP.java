package example.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AOP {
    @Pointcut("execution(* example.websocket..*.*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint point) {
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        log.info("正在調用 " + className + " - " + methodName + " 方法");
    }

//    @AfterReturning(value = "pointcut()", returning = "result")
//    public void afterReturn(JoinPoint point, Object result) {
//        if (result != null) {
//            log.info("結果: " + result);
//        } else {
//            log.info("結果:　" + null);
//        }
//    }
}
