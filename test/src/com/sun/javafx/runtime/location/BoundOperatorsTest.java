package com.sun.javafx.runtime.location;

import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * BoundOperatorsTest
 *
 * @author Brian Goetz
 */
public class BoundOperatorsTest extends JavaFXTestCase {

    public void testIndirectIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final IntVariable i = IntVariable.make(1);
        IntLocation ifLoc = BoundOperators.makeBoundIf(false, b,
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       },
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return -i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       });
        CountingListener cl = new CountingListener();
        ifLoc.addChangeListener(cl);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(true);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(false);
        assertEquals(-1, ifLoc.getAsInt());
        assertEquals(1, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        b.set(true);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(3);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(5);
        assertEquals(5, ifLoc.getAsInt());
        assertEquals(4, cl.count);
    }

    public void testIndirectSeqIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final SequenceVariable<Integer> x = SequenceVariable.make(TypeInfo.Integer, Sequences.make(TypeInfo.Integer, 1, 2, 3));
        final SequenceVariable<Integer> y = SequenceVariable.make(TypeInfo.Integer, Sequences.make(TypeInfo.Integer, 4, 5, 6));
        SequenceLocation<Integer> ifLoc = BoundOperators.makeBoundIf(TypeInfo.Integer, false, b,
                                                                     new Function0<SequenceLocation<Integer>>() {
                                                                         public SequenceLocation<Integer> invoke() {
                                                                             return x;
                                                                         }
                                                                     },
                                                                     new Function0<SequenceLocation<Integer>>() {
                                                                         public SequenceLocation<Integer> invoke() {
                                                                             return y;
                                                                         }
                                                                     });
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        ifLoc.addChangeListener(hl);
        assertEqualsAndClear(hl);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3);

        x.insert(4);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4);
        assertEqualsAndClear(hl, "[3, 2] => [ 4 ]");

        b.set(false);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEqualsAndClear(hl, "[0, 3] => [ 4, 5, 6 ]");

        x.insert(5);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEqualsAndClear(hl);

        y.insert(7);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6, 7);
        assertEqualsAndClear(hl, "[3, 2] => [ 7 ]");

        b.set(true);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4, 5);
        assertEqualsAndClear(hl, "[0, 3] => [ 1, 2, 3, 4, 5 ]");
    }

    public void testBoundAdd() {
        IntLocation il = IntVariable.make(1);
        DoubleLocation dl = DoubleVariable.make(1);
        FloatLocation fl = FloatVariable.make(1);
        LongLocation ll = LongVariable.make(1);

        IntLocation il_i = BoundOperators.op_int(il, ll, BoundOperators.NumericArithmeticOperator.PLUS);
        assertEquals(il_i.getAsInt(), 2);
        il.setAsInt(2);
        assertEquals(il_i.getAsInt(), 3);
        ll.setAsLong(2);
        assertEquals(il_i.getAsInt(), 4);

        FloatLocation fd_f = BoundOperators.op_float(fl, dl, BoundOperators.NumericArithmeticOperator.PLUS);
        assertEquals(fd_f.getAsInt(), 2);
        fl.setAsFloat(2);
        assertEquals(fd_f.getAsInt(), 3);
        dl.setAsDouble(2);
        assertEquals(fd_f.getAsInt(), 4);
    }

    public void testBoundOps() {

        class LocHolder<T extends Number> {
            ObjectLocation<T> loc;
            T[] values;
            int pos;

            LocHolder(ObjectLocation<T> loc, T... values) {
                this.loc = loc;
                this.values = values;
            }

            void reset() { pos = 0; advance(); }
            void advance() { loc.set(values[pos++]); }
        }

        class OpResults<T> {
            ObjectLocation<T> loc;
            T[] results;

            OpResults(ObjectLocation<T> loc, T... results) {
                this.loc = loc;
                this.results = results;
            }
        }
        Map<BoundOperators.NumericArithmeticOperator, OpResults[]> opMap = new HashMap<BoundOperators.NumericArithmeticOperator, OpResults[]>();

        LocHolder[] locs = new LocHolder[] {
                new LocHolder<Integer>(IntVariable.make(), 0, 1, 2),
                new LocHolder<Integer>(IntVariable.make(), 0, 1, 2),
                new LocHolder<Double>(DoubleVariable.make(), 0.0, 1.0, 2.0),
                new LocHolder<Double>(DoubleVariable.make(), 0.0, 1.0, 2.0),
                new LocHolder<Float>(FloatVariable.make(), 0.0f, 1.0f, 2.0f),
                new LocHolder<Float>(FloatVariable.make(), 0.0f, 1.0f, 2.0f),
                new LocHolder<Long>(LongVariable.make(), 0L, 1L, 2L),
                new LocHolder<Long>(LongVariable.make(), 0L, 1L, 2L),
                new LocHolder<Short>(ShortVariable.make(), (short) 0, (short) 1, (short) 2), 
                new LocHolder<Byte>(ByteVariable.make(), (byte) 0, (byte) 1, (byte) 2),
        };

        opMap.put(BoundOperators.NumericArithmeticOperator.PLUS,
                  new OpResults[] {
                          new OpResults<Integer>(null, 0, 1, 2, 3, 4),
                          new OpResults<Double>(null, 0.0, 1.0, 2.0, 3.0, 4.0),
                          new OpResults<Float>(null, 0.0f, 1.0f, 2.0f, 3.0f, 4.0f),
                          new OpResults<Long>(null, 0L, 1L, 2L, 3L, 4L)
                  });
        opMap.put(BoundOperators.NumericArithmeticOperator.MINUS,
                  new OpResults[] {
                          new OpResults<Integer>(null, 0, 1, 0, 1, 0),
                          new OpResults<Double>(null, 0.0, 1.0, 0.0, 1.0, 0.0),
                          new OpResults<Float>(null, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f),
                          new OpResults<Long>(null, 0L, 1L, 0L, 1L, 0L)
                  });
        opMap.put(BoundOperators.NumericArithmeticOperator.TIMES,
                  new OpResults[] {
                          new OpResults<Integer>(null, 0, 0, 1, 2, 4),
                          new OpResults<Double>(null, 0.0, 0.0, 1.0, 2.0, 4.0),
                          new OpResults<Float>(null, 0.0f, 0.0f, 1.0f, 2.0f, 4.0f),
                          new OpResults<Long>(null, 0L, 0L, 1L, 2L, 4L)
                  });

        for (int i=0; i<locs.length-1; i++)
            for (int j=i+1; j<locs.length; j++)
                for (BoundOperators.NumericArithmeticOperator op : opMap.keySet()) {
                    LocHolder left = locs[i];
                    LocHolder right = locs[j];
                    left.reset();
                    right.reset();
                    IntLocation il = BoundOperators.op_int((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    DoubleLocation dl = BoundOperators.op_double((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    FloatLocation fl = BoundOperators.op_float((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    LongLocation ll = BoundOperators.op_long((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    CountingListener cl1 = new CountingListener();
                    CountingListener cl2 = new CountingListener();
                    CountingListener cl3 = new CountingListener();
                    CountingListener cl4 = new CountingListener();
                    il.addChangeListener(cl1);
                    dl.addChangeListener(cl2);
                    fl.addChangeListener(cl3);
                    ll.addChangeListener(cl4);

                    OpResults[] results = opMap.get(op);
                    results[0].loc = il;
                    results[1].loc = dl;
                    results[2].loc = fl;
                    results[3].loc = ll;

                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[0]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[1]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[2]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[3]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[4]);
                    assertEquals(4, cl1.count);
                    assertEquals(4, cl2.count);
                    assertEquals(4, cl3.count);
                    assertEquals(4, cl4.count);
                }

        // Now do the same thing wrapping each NumericLocation with an object-to-numeric wrapper
        for (int i=0; i<locs.length-1; i++)
            for (int j=i+1; j<locs.length; j++)
                for (BoundOperators.NumericArithmeticOperator op : opMap.keySet()) {
                    LocHolder left = locs[i];
                    LocHolder right = locs[j];
                    left.reset();
                    right.reset();
                    IntLocation il = BoundOperators.op_int(Locations.asNumericLocation(left.loc), Locations.asNumericLocation(right.loc), op);
                    DoubleLocation dl = BoundOperators.op_double(Locations.asNumericLocation(left.loc), Locations.asNumericLocation(right.loc), op);
                    FloatLocation fl = BoundOperators.op_float(Locations.asNumericLocation(left.loc), Locations.asNumericLocation(right.loc), op);
                    LongLocation ll = BoundOperators.op_long(Locations.asNumericLocation(left.loc), Locations.asNumericLocation(right.loc), op);
                    CountingListener cl1 = new CountingListener();
                    CountingListener cl2 = new CountingListener();
                    CountingListener cl3 = new CountingListener();
                    CountingListener cl4 = new CountingListener();
                    il.addChangeListener(cl1);
                    dl.addChangeListener(cl2);
                    fl.addChangeListener(cl3);
                    ll.addChangeListener(cl4);

                    OpResults[] results = opMap.get(op);
                    results[0].loc = il;
                    results[1].loc = dl;
                    results[2].loc = fl;
                    results[3].loc = ll;

                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[0]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[1]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[2]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[3]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[4]);
                    assertEquals(4, cl1.count);
                    assertEquals(4, cl2.count);
                    assertEquals(4, cl3.count);
                    assertEquals(4, cl4.count);
                }
    }

    public void testBoundCmp() {
        class LocHolder<T extends Number> {
            ObjectLocation<T> loc;
            T[] values;
            int pos;

            LocHolder(ObjectLocation<T> loc, T... values) {
                this.loc = loc;
                this.values = values;
            }

            void reset() { pos = 0; advance(); }
            void advance() { loc.set(values[pos++]); }
        }

        class OpResults<T> {
            ObjectLocation<T> loc;
            boolean[] results;

            OpResults(ObjectLocation<T> loc, boolean... results) {
                this.loc = loc;
                this.results = results;
            }
        }
        Map<BoundOperators.NumericComparisonOperator, OpResults[]> opMap = new HashMap<BoundOperators.NumericComparisonOperator, OpResults[]>();

        LocHolder[] locs = new LocHolder[] {
                new LocHolder<Integer>(IntVariable.make(), 0, 1, 2),
                new LocHolder<Integer>(IntVariable.make(), 0, 1, 2),
                new LocHolder<Double>(DoubleVariable.make(), 0.0, 1.0, 2.0),
                new LocHolder<Double>(DoubleVariable.make(), 0.0, 1.0, 2.0),
                new LocHolder<Float>(FloatVariable.make(), 0.0f, 1.0f, 2.0f),
                new LocHolder<Float>(FloatVariable.make(), 0.0f, 1.0f, 2.0f),
                new LocHolder<Long>(LongVariable.make(), 0L, 1L, 2L),
                new LocHolder<Long>(LongVariable.make(), 0L, 1L, 2L),
                new LocHolder<Short>(ShortVariable.make(), (short) 0, (short) 1, (short) 2),
                new LocHolder<Byte>(ByteVariable.make(), (byte) 0, (byte) 1, (byte) 2),
        };

        opMap.put(BoundOperators.NumericComparisonOperator.CMP_EQ,
                  new OpResults[] {
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                  });
        opMap.put(BoundOperators.NumericComparisonOperator.CMP_NE,
                  new OpResults[] {
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                  });
        opMap.put(BoundOperators.NumericComparisonOperator.CMP_GE,
                  new OpResults[] {
                          new OpResults<Boolean>(null, true, true, true, true, true),
                          new OpResults<Boolean>(null, true, true, true, true, true),
                          new OpResults<Boolean>(null, true, true, true, true, true),
                          new OpResults<Boolean>(null, true, true, true, true, true),
                  });
        opMap.put(BoundOperators.NumericComparisonOperator.CMP_GT,
                  new OpResults[] {
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                          new OpResults<Boolean>(null, false, true, false, true, false),
                  });
        opMap.put(BoundOperators.NumericComparisonOperator.CMP_LE,
                  new OpResults[] {
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                          new OpResults<Boolean>(null, true, false, true, false, true),
                  });
        opMap.put(BoundOperators.NumericComparisonOperator.CMP_LT,
                  new OpResults[] {
                          new OpResults<Boolean>(null, false, false, false, false, false),
                          new OpResults<Boolean>(null, false, false, false, false, false),
                          new OpResults<Boolean>(null, false, false, false, false, false),
                          new OpResults<Boolean>(null, false, false, false, false, false),
                  });

        for (int i=0; i<locs.length-1; i++)
            for (int j=i+1; j<locs.length; j++)
                for (BoundOperators.NumericComparisonOperator op : opMap.keySet()) {
                    LocHolder left = locs[i];
                    LocHolder right = locs[j];
                    left.reset();
                    right.reset();
                    BooleanLocation il = BoundOperators.cmp_int((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    BooleanLocation dl = BoundOperators.cmp_double((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    BooleanLocation fl = BoundOperators.cmp_float((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    BooleanLocation ll = BoundOperators.cmp_long((NumericLocation) locs[i].loc, (NumericLocation) locs[j].loc, op);
                    CountingListener cl1 = new CountingListener();
                    CountingListener cl2 = new CountingListener();
                    CountingListener cl3 = new CountingListener();
                    CountingListener cl4 = new CountingListener();
                    il.addChangeListener(cl1);
                    dl.addChangeListener(cl2);
                    fl.addChangeListener(cl3);
                    ll.addChangeListener(cl4);

                    OpResults[] results = opMap.get(op);
                    results[0].loc = il;
                    results[1].loc = dl;
                    results[2].loc = fl;
                    results[3].loc = ll;

                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[0]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[1]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[2]);
                    left.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[3]);
                    right.advance();
                    for (OpResults or : results) assertEquals(or.loc.get(), or.results[4]);
                    assertEquals(4, cl1.count);
                    assertEquals(4, cl2.count);
                    assertEquals(4, cl3.count);
                    assertEquals(4, cl4.count);
                }
    }
}
