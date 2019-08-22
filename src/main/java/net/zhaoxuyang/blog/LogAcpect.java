package net.zhaoxuyang.blog;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;

/**
 * <pre>
 * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)throws-pattern?)
 * 括号中各个pattern分别表示：
 *
 * 修饰符匹配（modifier-pattern?）
 * 返回值匹配（ret-type-pattern）可以为*表示任何返回值,全路径的类名等
 * 类路径匹配（declaring-type-pattern?）
 * 方法名匹配（name-pattern）可以指定方法名 或者 *代表所有, set* 代表以set开头的所有方法
 * 参数匹配（(param-pattern)）可以指定具体的参数类型，多个参数间用“,”隔开，各个参数也可以用“*”来表示匹配任意类型的参数，如(String)表示匹配一个String参数的方法；(*,String) 表示匹配有两个参数的方法，第一个参数可以是任意类型，而第二个参数是String类型；可以用(..)表示零个或多个任意参数
 * 异常类型匹配（throws-pattern?）
 * 其中后面跟着“?”的是可选项
 * </pre>
 *
 * @author zhaoxuyang
 */
@Aspect
@Component
public class LogAcpect {

    final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 设置切入点
     */
    @Pointcut("execution(public * net.zhaoxuyang.blog.controller..*.*(..))")
    public void webController() {
    }

    /**
     * 统计方法耗时（ms）
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("webController()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();
        String className = pjp.getTarget().getClass().getCanonicalName();
        String methodName = pjp.getSignature().getName();

        Object output = pjp.proceed();
        long elapsedTime = System.nanoTime() - startTime;

        LOG.info(String.format("方法[%s#%s]耗时(ms)：%f", className, methodName,
                new BigDecimal(elapsedTime).divide(new BigDecimal(1_000_000))));

        return output;
    }

    /**
     * 在请求之前打印HTTP请求等信息
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webController()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容

        Signature signature = joinPoint.getSignature();
        LOG.info(String.format("请求URL: %s", request.getRequestURL().toString()));
        LOG.info(String.format("请求方法: %s", request.getMethod()));
        LOG.info(String.format("远程地址: %s", request.getRemoteAddr()));
        LOG.info(String.format("当前方法: %s.%s", signature.getDeclaringTypeName(), signature.getName()));
        LOG.info(String.format("参数列表: %s", Arrays.toString(joinPoint.getArgs())));
    }

}
