package ua.edu.ucu.stream;

import ua.edu.ucu.exception.TheStreamIsClosed;
import ua.edu.ucu.function.*;
import ua.edu.ucu.iterator.Queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AsIntStream implements IntStream {
    private Iterator<Integer> streamItr;
    private Queue<Integer> q;
    private boolean closed = false;

    private AsIntStream(int... values) {
        int n = values.length;
        Integer[] val = new Integer[n];
        for (int i = 0; i < n; i++) {
            val[i] = values[i];
        }
        q = new Queue<>(val);
        streamItr = q.iterator();
    }
    private AsIntStream(Iterator<Integer> itr) {
        this.streamItr = itr;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    private void checkEmptiness() {
        if (!streamItr.hasNext()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkTerminated() {
        if (closed) {
            throw new TheStreamIsClosed();
        }
    }

    //To change body of generated methods, choose Tools | Templates.
    @Override
    public Double average() {
        checkTerminated();
        checkEmptiness();
        int sumOfEls = 0;
        int n = 0;
        while (streamItr.hasNext()) {
            sumOfEls += streamItr.next();
            n++;
        }
        closed = true;
        return (double) (sumOfEls / n);
    }

    @Override
    public Integer max() {
        checkTerminated();
        checkEmptiness();
        int firstEl = streamItr.next();
        IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int apply(int left, int right) {
                return Math.max(left, right);
            }
        };
        return reduce(firstEl, op);
    }

    @Override
    public Integer min() {
        checkTerminated();
        checkEmptiness();
        int firstEl = streamItr.next();
        IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int apply(int left, int right) {
                return Math.min(left, right);
            }
        };
        return reduce(firstEl, op);
    }

    @Override
    public long count() {
        checkTerminated();
        long size = 0;
        while (streamItr.hasNext()) {
            size ++;
            streamItr.next();
        }
        closed = true;
        return size;
    }

    @Override
    public Integer sum() {
        checkTerminated();
        checkEmptiness();
        int sumOfEls = 0;
        while (streamItr.hasNext()) {
            sumOfEls += streamItr.next();
        }
        closed = true;
        return sumOfEls;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        checkTerminated();
        Iterator <Integer> iter = this.streamItr;
        return new AsIntStream(new Iterator<Integer>() {
            private Integer nextFiltered = null;
            @Override
            public boolean hasNext() {
                if (nextFiltered != null) {
                    return true;
                }
                while (iter.hasNext()) {
                    Integer prev = iter.next();
                    if (predicate.test(prev)) {
                        nextFiltered = prev;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Integer next() {
                if (nextFiltered == null) {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                Integer res = nextFiltered;
                nextFiltered = null;
                return res;
            }
        });
    }

    @Override
    public void forEach(IntConsumer action) {
        checkTerminated();
        while (streamItr.hasNext()) {
            action.accept(streamItr.next());
        }
        closed = true;
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        checkTerminated();
        Iterator <Integer> iter = this.streamItr;
        return new AsIntStream(new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return mapper.apply(iter.next());
            }
        });
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        checkTerminated();

        Iterator<Integer> iter = this.streamItr;

        return new AsIntStream(new Iterator<Integer>() {
            private Iterator<Integer> tempItr = new Queue<Integer>().iterator();
            @Override
            public boolean hasNext() {
                if (!tempItr.hasNext()) {
                    return iter.hasNext();
                }
                return true;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (tempItr.hasNext()) {
                    return tempItr.next();
                }
                Queue<Integer> q1 = new Queue<>();
                IntStream temp = func.applyAsIntStream(iter.next());
                int[] tempArr = temp.toArray();
                for (int el : tempArr) {
                    q1.enqueue(el);
                }
                tempItr = q1.iterator();

                return tempItr.next();
            }
        });
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        checkTerminated();
        if (!streamItr.hasNext()) {
            return identity;
        }
        int res = op.apply(identity, streamItr.next());
        while (streamItr.hasNext()) {
            res = op.apply(res, streamItr.next());
        }
        closed = true;
        return res;
    }

    @Override
    public int[] toArray() {
        checkTerminated();
        ArrayList<Integer> tempList = new ArrayList<>();
        while (streamItr.hasNext()) {
            tempList.add(streamItr.next());
        }
        closed = true;
        int n = tempList.size();
        int[] val = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = tempList.get(i);
        }
        return val;
    }


}
