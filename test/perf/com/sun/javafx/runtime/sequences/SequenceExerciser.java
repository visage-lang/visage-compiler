package com.sun.javafx.runtime.sequences;

import java.util.*;
import java.util.regex.*;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * SequenceExerciser
 *
 * @author Brian Goetz
 */
public class SequenceExerciser implements Test {
    private static Map<String, Class<? extends SequenceOpGenerator>> ops = new HashMap<String, Class<? extends SequenceOpGenerator>>();
    static int random = (int) System.nanoTime();
    static {
        ops.put("rr", RandomReadGenerator.class);
        ops.put("rw", RandomWriteGenerator.class);
        ops.put("ra", RandomAccessGenerator.class);
        ops.put("stream", SequenceAsStream.class);
    }

    public static int xorShift(int x) {
        x ^= (x << 6);
        x ^= (x >>> 21);
        x ^= (x << 7);
        return x;
    }

    public static int nextRandom(int bound) {
        int ret = random % bound;
        random = xorShift(random);
        return ret;
    }

    public TestResult start (String args) {
        Pattern p = Pattern.compile("\"(.+?)\"");
        Matcher m = p.matcher(args);
        ArrayList<String> parts = new ArrayList<String>();
        while (m.find()) {
            parts.add(m.group(1));
        }
        try {
            TestResult result = run(parts.toArray(new String[0]));
            return result;
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return TestResult.EMPTY;
        }
    }

    public static TestResult run(String[] args) throws IllegalAccessException, InstantiationException {
        long time = 0;
        int initialSize = Integer.parseInt(args[0]);
        SequenceLocation<Integer> seq = SequenceVariable.make(Integer.class, Sequences.range(1, initialSize));
        int sum = 0;
        long start = System.nanoTime();
        for (int i=1; i<args.length; i++) {
            String[] parts = args[i].split(" ");
            Class<? extends SequenceOpGenerator> g = ops.get(parts[0]);
            SequenceOpGenerator gg = g.newInstance();
            gg.parseOptions(parts, 1);
            sum += gg.doOp(seq);
            long end = System.nanoTime();
            time += (end-start);
            System.out.printf("Operation %s: time %d%n", args[i], (end-start));
            start = end;
        }
        return new TestResult((int)(time/1000000));
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        run(args);
    }
}

interface SequenceOpGenerator {
    public void parseOptions(String[] options, int startingPoint);
    public int doOp(SequenceLocation<Integer> seq);
}

class RandomReadGenerator implements SequenceOpGenerator {
    private int opCount;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++)
            sum += seq.get(SequenceExerciser.nextRandom(size));
        return sum;
    }
}

class RandomWriteGenerator implements SequenceOpGenerator {
    private int opCount;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int value = SequenceExerciser.nextRandom(10000);
            sum += value;
            seq.set(SequenceExerciser.nextRandom(size), value);
        }
        return sum;
    }
}

class RandomAccessGenerator implements SequenceOpGenerator {
    private int opCount;
    private int readPercentage;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        readPercentage = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int key = SequenceExerciser.nextRandom(100);
            if (key < readPercentage) {
                sum += seq.get(SequenceExerciser.nextRandom(size));
            }
            else {
                int value = SequenceExerciser.nextRandom(10000);
                sum += value;
                seq.set(SequenceExerciser.nextRandom(size), value);
            }
        }
        return sum;
    }
}

class SequenceAsStream implements SequenceOpGenerator {
    private int opCount;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            seq.insert(SequenceExerciser.nextRandom(10000));
            sum += seq.get(0);
            seq.delete(0);
        }
        return sum;
    }
}

