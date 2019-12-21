package ua.edu.ucu;
import org.junit.Before;
import org.junit.Test;
import ua.edu.ucu.exception.TheStreamIsClosed;
import ua.edu.ucu.function.IntBinaryOperator;
import ua.edu.ucu.stream.AsIntStream;
import ua.edu.ucu.stream.IntStream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AsIntStreamTest {

    private IntStream intStream;
    private int[] intArr;
    private int[] emptyArr;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        int[] emptyArr = {};
        this.intArr = intArr;
        this.emptyArr = emptyArr;
        intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testOf() {
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = intStream.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testOfEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        int[] result = st.toArray();
        assertArrayEquals(emptyArr, result);
    }

    @Test
    public void testAverage() {
        double expResult = 1.0;
        double result = intStream.average();
        assertEquals(expResult, result, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAverageEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        st.average();
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testAverageTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        intStream.average();
        intStream.average();
    }

    @Test
    public void testMax() {
        int expResult = 3;
        int result = intStream.max();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        st.max();
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testMaxTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        intStream.max();
        intStream.max();
    }

    @Test
    public void testMin() {
        int expResult = -1;
        int result = intStream.min();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        st.min();
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testMinTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        intStream.min();
        intStream.min();
    }

    @Test
    public void testCount() {
        long expResult = 5;
        long result = intStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testCountEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        long expResult = 0;
        long result = st.count();
        assertEquals(expResult, result);
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testCountTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        intStream.count();
        intStream.count();
    }

    @Test
    public void testSum() {
        long expResult = 5;
        long result = intStream.sum();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSumEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        st.sum();
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testSumTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        intStream.sum();
        intStream.sum();
    }

    @Test
    public void testReduce() {
        IntStream intStForEach = AsIntStream.of(intArr);
        IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int apply(int left, int right) {
                return left * right;
            }
        };
        int result = intStForEach.reduce(4, op);
        int expResult = 0;
        assertEquals(expResult, result);

    }

    @Test
    public void testReduceEmpty() {
        IntStream st = AsIntStream.of(emptyArr);
        IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int apply(int left, int right) {
                return left * right;
            }
        };
        int result = st.reduce(4, op);
        int expResult = 4;
        assertEquals(expResult, result);
    }

    @Test(expected = TheStreamIsClosed.class)
    public void testReduceTerminalException() {
        IntStream intStream = AsIntStream.of(intArr);
        IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int apply(int left, int right) {
                return left * right;
            }
        };
        intStream.reduce(4, op);
        intStream.reduce(4, op);
    }

    @Test
    public void testFilter() {
        IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3);
        int[] filterArr = {1, 2, 3};
        int[] res = intStream.filter(x -> x > 0).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testFilterEmptyRes() {
        IntStream intStream = AsIntStream.of(-1, 0, -1, -2, -3);
        int[] filterArr = {};
        int[] res = intStream.filter(x -> x > 0).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testFilterEmpty() {
        IntStream intStream = AsIntStream.of();
        int[] filterArr = {};
        int[] res = intStream.filter(x -> x > 0).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testMap() {
        IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3);
        int[] filterArr = {1, 0, 1, 4, 9};
        int[] res = intStream.map(x -> x * x).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testMapEmpty() {
        IntStream intStream = AsIntStream.of();
        int[] filterArr = {};
        int[] res = intStream.map(x -> x * x).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testFlatMap() {
        IntStream intStream = AsIntStream.of(1, 4, 9);
        int[] filterArr = {0, 1, 2, 3, 4, 5, 8, 9, 10};
        int[] res = intStream.flatMap(x -> AsIntStream.of(x - 1, x, x + 1)).toArray();
        assertArrayEquals(filterArr, res);
    }

    @Test
    public void testFlatMapEmpty() {
        IntStream intStream = AsIntStream.of();
        int[] filterArr = {};
        int[] res = intStream.flatMap(x -> AsIntStream.of(x - 1, x, x + 1)).toArray();
        assertArrayEquals(filterArr, res);
    }
}
