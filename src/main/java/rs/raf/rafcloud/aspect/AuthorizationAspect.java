package rs.raf.rafcloud.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import rs.raf.rafcloud.model.Role;

import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@Configuration
public class AuthorizationAspect {


    @Pointcut("@annotation(MyAuthorization)")
    void checkAuthorization(){
    }

    @Around("checkAuthorization()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        Object o = null;
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Class<?> clazz = methodSignature.getDeclaringType();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        MyAuthorization authorizationAnn = method.getAnnotation(MyAuthorization.class);
        Collection<Role> userRoles = (Collection<Role>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(Role r : userRoles){
            if(r.getRole().equals(authorizationAnn.authorization().toString()))
                o = jp.proceed();
        }
        if(o == null){
            o = new ResponseEntity<>("Unauthorized for " + authorizationAnn.authorization(), HttpStatus.UNAUTHORIZED);
        }

        return o;
    }
}
