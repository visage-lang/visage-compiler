package com.sun.javafx.runtime.sequences;

import java.util.*;

/**
 *
 * @author Michael Heinrichs
 */
public class Runner {

    public static Map<String, TestResult> runTests(Script script, int iterations) {
        Map<String, TestResult> result = new HashMap<String, TestResult>();
        final int length = script.tests.size();
        TestResult[] resultList = new TestResult[iterations];
        for (int test=0; test<length; test++) {
            for (int it=0; it<iterations; it++) 
                resultList[it] = script.tests.get(test).start(script.args.get(test));
            result.put(script.commands.get(test) + " " + script.args.get(test), TestResult.average(resultList));
        }
        return result;
    }
}
