package com.sun.javafx.functions;

public interface Function3<R, A1, A2, A3> extends Function<R> {
   R invoke(A1 x1, A2 x2, A2 x3);
}
