package net.kayega.core.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VSubCommandArguments {
    String commandName();
    String commandDescription();
    String commandPermission() default "";
}
