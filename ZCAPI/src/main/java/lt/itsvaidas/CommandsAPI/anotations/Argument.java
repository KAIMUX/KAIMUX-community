package lt.itsvaidas.CommandsAPI.anotations;

import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import lt.itsvaidas.CommandsAPI.argumentproviders.EmptyProvider;
import lt.itsvaidas.CommandsAPI.enums.ArgumentType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String key();
    Class<? extends ArgumentProvider> provider() default EmptyProvider.class;
    ArgumentType type() default ArgumentType.SINGLE;
}
