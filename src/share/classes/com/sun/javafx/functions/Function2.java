package com.sun.javafx.functions;

public interface Function2<R, A1, A2> extends Function<R> {
   R invoke(A1 x1, A2 x2);
}
