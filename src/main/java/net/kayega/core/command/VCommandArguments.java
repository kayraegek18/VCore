package net.kayega.core.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VCommandArguments {
    String commandName();
    String permission() default "";
    boolean isPlayerCommand() default true;
    boolean isHaveSubCommand() default false;
}
