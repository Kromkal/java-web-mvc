package com.vison.webmvc.framework;

import java.lang.annotation.*;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GetMapping {

    String path() default "";

}
