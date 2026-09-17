package it.michelemasetto.telegram.bots;

import java.util.function.IntFunction;

public class Util {

    private Util() {
    }

    @FunctionalInterface
    public interface ThrowingIntFunction<R> {

        R apply(int value) throws Exception;
    }

    public static <R> IntFunction<R> throwingIntFunction(ThrowingIntFunction<R> func) {
        return (r) -> {
            try {
                return func.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
