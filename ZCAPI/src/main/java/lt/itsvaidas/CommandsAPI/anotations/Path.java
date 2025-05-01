package lt.itsvaidas.CommandsAPI.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String path() default "";
    String permission() default "";
    String description();
    boolean translatable() default false;
    Argument[] arguments() default {};
}
