package com.sun.javafx.runtime.sequences;

import java.util.*;
import java.util.regex.*;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.TypeInfo;

/**
 * SequenceExerciser
 *
 * @author Brian Goetz
 */
public class SequenceExerciser implements Test {
    private static Map<String, Class<? extends SequenceOpGenerator>> ops = new HashMap<String, Class<? extends SequenceOpGenerator>>();
    static int random = (int) System.nanoTime();
    static {
        ops.put("rr1", RandomReadGenerator1.class);
        ops.put("rw1", RandomWriteGenerator1.class);
        ops.put("rw", RandomWriteGenerator.class);
        ops.put("ra1", RandomAccessGenerator1.class);
        ops.put("ra", RandomAccessGenerator.class);
        ops.put("rd1", RandomDeleteGenerator1.class);
        ops.put("rd", RandomDeleteGenerator.class);
        ops.put("ri1", RandomInsertGenerator1.class);
        ops.put("ri", RandomInsertGenerator.class);
        ops.put("rdi1", RandomInsertAndDeleteGenerator1.class);
        ops.put("rdi", RandomInsertAndDeleteGenerator.class);
        ops.put("stream1", SequenceAsStream1.class);
        ops.put("stream", SequenceAsStream.class);
        ops.put("iterW1", IterationAfterWrite1.class);
        ops.put("iterW", IterationAfterWrite.class);
        ops.put("iterDI1", IterationAfterInsertAndDelete1.class);
        ops.put("iterDI", IterationAfterInsertAndDelete.class);
    }

    public static int xorShift(int x) {
        x ^= (x << 6);
        x ^= (x >>> 21);
        x ^= (x << 7);
        return x;
    }

    public static int nextRandom(int bound) {
        random = xorShift(random);
        return Math.abs(random % bound);
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
            System.err.println(ex);
            return TestResult.EMPTY;
        }
    }

    public static TestResult run(String[] args) throws IllegalAccessException, InstantiationException {
        long time = 0;
        int initialSize = Integer.parseInt(args[0]);
        SequenceLocation<Integer> seq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, initialSize));
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
            System.out.printf("Operation(%s) %s: time %d%n", args[0], args[i], (end-start)/1000000);
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

class RandomReadGenerator1 implements SequenceOpGenerator {
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

class RandomWriteGenerator1 implements SequenceOpGenerator {
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

class RandomWriteGenerator implements SequenceOpGenerator {
    private int opCount;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxChunkSize = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int pos = SequenceExerciser.nextRandom(size);
            int chunkSize = SequenceExerciser.nextRandom(maxChunkSize) + 1;
            int value = SequenceExerciser.nextRandom(10000);
            sum += value;
            seq.replaceSlice(pos, pos+chunkSize, Sequences.rangeExclusive(value, value+chunkSize));
        }
        return sum;
    }
}

class RandomAccessGenerator1 implements SequenceOpGenerator {
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

class RandomAccessGenerator implements SequenceOpGenerator {
    private int opCount;
    private int readPercentage;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        readPercentage = Integer.parseInt(options[start+1]);
        maxChunkSize = Integer.parseInt(options[start+2]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int pos = SequenceExerciser.nextRandom(size);
            int chunkSize = SequenceExerciser.nextRandom(maxChunkSize) + 1;
            int key = SequenceExerciser.nextRandom(100);
            if (key < readPercentage) {
                sum += seq.getSlice(pos, pos+chunkSize).get(0);
            } else {
                int value = SequenceExerciser.nextRandom(10000);
                sum += value;
                seq.replaceSlice(pos, pos+chunkSize, Sequences.rangeExclusive(value, value+chunkSize));
            }
        }
        return sum;
    }
}

class RandomDeleteGenerator1 implements SequenceOpGenerator {
    private int opCount;
    private int deletePercentage;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        deletePercentage = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int minSize = size * (100-deletePercentage) / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            SequenceLocation<Integer> localSeq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, size));
            for (int curSize = size; curSize > minSize; ) {
                int position = SequenceExerciser.nextRandom(curSize--);
                sum += localSeq.get(position);
                localSeq.delete(position);
            }
        }
        return sum;
    }
}

class RandomDeleteGenerator implements SequenceOpGenerator {
    private int opCount;
    private int deletePercentage;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        deletePercentage = Integer.parseInt(options[start+1]);
        maxChunkSize = Integer.parseInt(options[start+2]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int minSize = size * (100-deletePercentage) / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            SequenceLocation<Integer> localSeq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, size));
            for (int curSize = size+1; curSize > minSize; ) {
                int chunkSize = SequenceExerciser.nextRandom(maxChunkSize);
                curSize -= chunkSize;
                int position = SequenceExerciser.nextRandom(curSize);
                sum += localSeq.get(position);
                localSeq.deleteSlice(position, position+chunkSize);
            }
        }
        return sum;
    }
}

class RandomInsertGenerator1 implements SequenceOpGenerator {
    private int opCount;
    private int insertPercentage;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        insertPercentage = 100 + Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int maxSize = size * insertPercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            SequenceLocation<Integer> localSeq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, size));
            for (int curSize = size; curSize < maxSize; ) {
                int value = SequenceExerciser.nextRandom(10000);
                sum += value;
                localSeq.insertBefore(value, SequenceExerciser.nextRandom(++curSize));
            }
        }
        return sum;
    }
}

class RandomInsertGenerator implements SequenceOpGenerator {
    private int opCount;
    private int insertPercentage;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        insertPercentage = 100 + Integer.parseInt(options[start+1]);
        maxChunkSize = Integer.parseInt(options[start+2]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int maxSize = size * insertPercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            SequenceLocation<Integer> localSeq = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, size));
            for (int curSize = size+1; curSize < maxSize; ) {
                int value = SequenceExerciser.nextRandom(10000);
                sum += value;
                int position = SequenceExerciser.nextRandom(curSize);
                int chunkSize = SequenceExerciser.nextRandom(maxChunkSize);
                curSize += chunkSize;
                localSeq.insertBefore(Sequences.range(value, value+chunkSize), position);
            }
        }
        return sum;
    }
}

class RandomInsertAndDeleteGenerator1 implements SequenceOpGenerator {
    private int opCount;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int position = SequenceExerciser.nextRandom(size);
            sum += seq.get(position);
            seq.delete(position);
            
            int value = SequenceExerciser.nextRandom(10000);
            sum += value;
            seq.insertBefore(value, SequenceExerciser.nextRandom(size));
        }
        return sum;
    }
}

class RandomInsertAndDeleteGenerator implements SequenceOpGenerator {
    private int opCount;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxChunkSize = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int chunkSize = SequenceExerciser.nextRandom(maxChunkSize);

            int position = SequenceExerciser.nextRandom(size);
            sum += seq.get(position);
            seq.deleteSlice(position, position+chunkSize);

            int value = SequenceExerciser.nextRandom(10000);
            sum += value;
            position = SequenceExerciser.nextRandom(size + 1 - chunkSize);
            seq.insertBefore(Sequences.range(value, value+chunkSize), position);
        }
        return sum;
    }
}

class SequenceAsStream1 implements SequenceOpGenerator {
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

class SequenceAsStream implements SequenceOpGenerator {
    private int opCount;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxChunkSize = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int minSize = size / 2;
        int maxSize = (3*size) / 2;
        int sum = 0;
        
        for (int i=0; i<opCount; i++) {
            int chunkSize = SequenceExerciser.nextRandom(maxChunkSize);
            if (size+chunkSize < maxSize) {
                for (int j=0; j<chunkSize; j++)
                    seq.insert(SequenceExerciser.nextRandom(10000));
            }
            chunkSize = SequenceExerciser.nextRandom(maxChunkSize);
            if (size-chunkSize > minSize) {
                for (int j=0; j<chunkSize; j++) {
                    sum += seq.get(0);
                    seq.delete(0);
                }
            }
        }
        return sum;
    }
}

class IterationAfterWrite1 implements SequenceOpGenerator {
    private int opCount;
    private int writePercentage;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        writePercentage = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int writeOps = size * writePercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            for (int j=0; j<writeOps; j++) {
                seq.set(SequenceExerciser.nextRandom(size), SequenceExerciser.nextRandom(10000));
            }
            
            Iterator<Integer> it = seq.get().iterator();
            while (it.hasNext()) {
                sum += it.next();
            }
        }
        return sum;
    }
}

class IterationAfterWrite implements SequenceOpGenerator {
    private int opCount;
    private int maxWritePercentage;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxWritePercentage = Integer.parseInt(options[start+1]);
        maxChunkSize = Integer.parseInt(options[start+2]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int maxWriteOps = size * maxWritePercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int writeCount = SequenceExerciser.nextRandom(maxWriteOps);
            for (int j=0; j<writeCount; j++) {
                int pos = SequenceExerciser.nextRandom(size);
                int chunkSize = SequenceExerciser.nextRandom(maxChunkSize) + 1;
                int value = SequenceExerciser.nextRandom(10000);
                seq.replaceSlice(pos, pos+chunkSize, Sequences.rangeExclusive(value, value+chunkSize));
            }
            
            Iterator<Integer> it = seq.get().iterator();
            while (it.hasNext()) {
                sum += it.next();
            }
        }
        return sum;
    }
}

class IterationAfterInsertAndDelete1 implements SequenceOpGenerator {
    private int opCount;
    private int maxWritePercentage;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxWritePercentage = Integer.parseInt(options[start+1]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int maxWriteOps = size * maxWritePercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int writeCount = SequenceExerciser.nextRandom(maxWriteOps);
            for (int j=0; j<writeCount; j++) {
                seq.delete(SequenceExerciser.nextRandom(size));
                seq.insertBefore(SequenceExerciser.nextRandom(10000), SequenceExerciser.nextRandom(size));
            }
            
            Iterator<Integer> it = seq.get().iterator();
            while (it.hasNext()) {
                sum += it.next();
            }
        }
        return sum;
    }
}

class IterationAfterInsertAndDelete implements SequenceOpGenerator {
    private int opCount;
    private int maxWritePercentage;
    private int maxChunkSize;

    public void parseOptions(String[] options, int start) {
        opCount = Integer.parseInt(options[start]);
        maxWritePercentage = Integer.parseInt(options[start+1]);
        maxChunkSize = Integer.parseInt(options[start+2]);
    }

    public int doOp(SequenceLocation<Integer> seq) {
        int size = seq.get().size();
        int maxWriteOps = size * maxWritePercentage / 100;
        int sum = 0;
        for (int i=0; i<opCount; i++) {
            int writeCount = SequenceExerciser.nextRandom(maxWriteOps);
            for (int j=0; j<writeCount; j++) {
                int chunkSize = SequenceExerciser.nextRandom(maxChunkSize);

                int position = SequenceExerciser.nextRandom(size);
                seq.deleteSlice(position, position+chunkSize);

                int value = SequenceExerciser.nextRandom(10000);
                position = SequenceExerciser.nextRandom(size + 1 - chunkSize);
                seq.insertBefore(Sequences.range(value, value+chunkSize), position);
            }
            
            Iterator<Integer> it = seq.get().iterator();
            while (it.hasNext()) {
                sum += it.next();
            }
        }
        return sum;
    }
}
