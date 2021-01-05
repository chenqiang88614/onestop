package com.onestop.common.conf;

import com.onestop.common.annotation.RedisTranStartPoint;
import com.onestop.common.util.RedisLocalData;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Administrator
 * @describe 用于redis做mybatisPlus第三方缓存是分页缓存
 */
@Aspect
@Component
public class RedisTranStartPointAspect {
    private static final Logger log = LoggerFactory.getLogger(RedisTranStartPointAspect.class);

    // 配置织入点
    @Pointcut(value = "@annotation(com.onestop.common.annotation.RedisTranStartPoint)")
    public void tranPointcut() {
    }


    @Before("tranPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            RedisTranStartPoint controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            RedisLocalData.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterReturning(pointcut = "tranPointcut()")
    public void doAfterReturn(JoinPoint joinPoint) {
        clean(joinPoint);
    }


    @AfterThrowing(value = "tranPointcut()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, Exception e) {
        clean(joinPoint);
    }

    private void clean(JoinPoint joinPoint) {
        try {
            RedisTranStartPoint anno = getAnnotationLog(joinPoint);
            if (anno != null) {
                RedisLocalData.clean();
            }
        } catch (Exception ex) {
            RedisLocalData.clean();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private RedisTranStartPoint getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(RedisTranStartPoint.class);
        }
        return null;
    }
}
