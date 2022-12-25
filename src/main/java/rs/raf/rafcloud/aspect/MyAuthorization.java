package rs.raf.rafcloud.aspect;


import rs.raf.rafcloud.permissions.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAuthorization {
    RoleEnum authorization();
}
