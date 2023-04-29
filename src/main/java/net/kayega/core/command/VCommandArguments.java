package net.kayega.core.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VCommandArguments {
    String commandName();
    boolean isPlayerCommand() default true;
    boolean registerOnStart() default true;
}
