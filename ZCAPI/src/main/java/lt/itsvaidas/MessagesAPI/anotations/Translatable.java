package lt.itsvaidas.MessagesAPI.anotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Translatable {
    boolean global() default false;
    String plugin();
}
