package ru.mehoil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link NumbersContainer} tests.
 *
 * @author Mikhail Dorokhov
 * @since 1.0
 */
class NumbersContainerTest {

    private static final long[] INITIAL_ARRAY = new long[]{1, 2, 3, 4, 5};

    @Test
    void testCreationEmpty() {
        final var nc = new NumbersContainer();
        assertTrue(nc.isEmpty());
        assertEquals(0, nc.getSize());
    }

    @Test
    void testCreationWithArray() {
        final var arr = new long[]{1, 2, 3, 4, 5};
        final var nc = new NumbersContainer(arr);

        assertFalse(nc.isEmpty());
        assertEquals(5, nc.getSize());

        for (int i = 0; i < nc.getSize(); i++) {
            assertEquals(arr[i], nc.get(i));
        }
    }

    @Test
    void testPutFirst() {
        final long numToInsert = 9999;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.putFirst(9999);

        assertEquals(INITIAL_ARRAY.length + 1, nc.getSize());
        assertEquals(numToInsert, nc.getFirst());
    }

    @Test
    void testPutLast() {
        final long numToInsert = 9999;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.putLast(numToInsert);

        assertEquals(INITIAL_ARRAY.length + 1, nc.getSize());
        assertEquals(numToInsert, nc.getLast());
    }

    @Test
    void testPutMiddle() {
        final long numToInsert = 9999;
        final int middleIndex = INITIAL_ARRAY.length / 2;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.put(middleIndex, numToInsert);

        assertEquals(INITIAL_ARRAY.length + 1, nc.getSize());
        assertEquals(numToInsert, nc.get(middleIndex));
    }

    @Test
    void testPutAll() {
        final var numbersToPut = new long[]{7, 8, 9, 10, 11, 12, 13};
        final var nc = new NumbersContainer(INITIAL_ARRAY);
        nc.putAll(numbersToPut);

        assertEquals(INITIAL_ARRAY.length + numbersToPut.length, nc.getSize());
        for (int i = 0; i < INITIAL_ARRAY.length; i++) {
            assertEquals(INITIAL_ARRAY[i], nc.get(i));
        }
        for (int i = 0; i < numbersToPut.length; i++) {
            assertEquals(numbersToPut[i], nc.get(INITIAL_ARRAY.length + i));
        }
    }

    @Test
    void testPutManyNumbers() {
        final var nc = new NumbersContainer();

        final int bound = 16_384;
        for (int i = 0; i < bound; i++) {
            nc.putLast(i);
        }

        assertEquals(bound, nc.getSize());
    }

    @Test
    void testRemainsConsistentAfterMiddlePut() {
        final long numToInsert = 9999;
        final int middleIndex = INITIAL_ARRAY.length / 2;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.put(middleIndex, numToInsert);

        int j = 0;
        for (int i = 0; i < nc.getSize(); i++) {
            if (i == middleIndex) {
                assertEquals(numToInsert, nc.get(i));
            } else {
                assertEquals(INITIAL_ARRAY[j++], nc.get(i));
            }
        }
    }

    @Test
    void testRemoveFirst() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.removeFirst();

        final int initArraySize = INITIAL_ARRAY.length;
        assertEquals(initArraySize - 1, nc.getSize());
        assertEquals(INITIAL_ARRAY[1], nc.getFirst());
    }

    @Test
    void testRemoveLast() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.removeLast();

        final int initArraySize = INITIAL_ARRAY.length;
        assertEquals(initArraySize - 1, nc.getSize());
        assertEquals(INITIAL_ARRAY[initArraySize - 2], nc.getLast());
    }

    @Test
    void testRemoveMiddle() {
        final int middleIndex = INITIAL_ARRAY.length / 2;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.remove(middleIndex);
        assertEquals(INITIAL_ARRAY.length - 1, nc.getSize());
    }

    @Test
    void testRemainsConsistentAfterMiddleRemoval() {
        final int middleIndex = INITIAL_ARRAY.length / 2;
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        nc.remove(middleIndex);

        int j = 0;
        for (int i = 0; i < nc.getSize(); i++) {
            if (i != middleIndex) {
                assertEquals(INITIAL_ARRAY[j], nc.get(i));
            } else {
                assertEquals(INITIAL_ARRAY[++j], nc.get(i));
            }
            j++;
        }
    }

    @Test
    void testGetFirst() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);
        assertEquals(INITIAL_ARRAY[0], nc.getFirst());
    }

    @Test
    void testGetLast() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);
        assertEquals(INITIAL_ARRAY[INITIAL_ARRAY.length - 1], nc.getLast());
    }

    @Test
    void testGetMiddle() {
        final int middleIndex = INITIAL_ARRAY.length / 2;
        final var nc = new NumbersContainer(INITIAL_ARRAY);
        assertEquals(INITIAL_ARRAY[middleIndex], nc.get(middleIndex));
    }

    @Test
    void testGetSize() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        assertEquals(INITIAL_ARRAY.length, nc.getSize());

        final int newNumbersCount = 13;
        for (int i = 0; i < newNumbersCount; i++) {
            nc.putLast(i + newNumbersCount);
        }

        assertEquals(INITIAL_ARRAY.length + newNumbersCount, nc.getSize());

        final int removalCount = 4;
        for (int i = 0; i < removalCount; i++) {
            nc.removeFirst();
        }

        assertEquals(INITIAL_ARRAY.length + newNumbersCount - removalCount, nc.getSize());
    }

    @Test
    void testIsEmpty() {
        final var nc = new NumbersContainer();

        assertTrue(nc.isEmpty());

        nc.putFirst(1);
        nc.putLast(4);

        assertFalse(nc.isEmpty());

        nc.clear();

        assertTrue(nc.isEmpty());
    }

    @Test
    void testClear() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        assertFalse(nc.isEmpty());

        nc.clear();

        assertTrue(nc.isEmpty());
        assertEquals(0, nc.getSize());
    }

    @Test
    void testCannotUseNegativeIndices() {
        final var nc = new NumbersContainer(INITIAL_ARRAY);

        assertThrows(IllegalArgumentException.class, () -> nc.get(-1));
        assertThrows(IllegalArgumentException.class, () -> nc.put(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> nc.remove(-1));
    }

    @Test
    void testEquals() {
        var nc1 = new NumbersContainer(new long[]{1, 2, 3});
        var nc2 = new NumbersContainer(new long[]{1, 2, 3});
        assertEquals(nc1, nc2);
    }

    @Test
    void testHashCodeConsistency() {
        NumbersContainer nc1 = new NumbersContainer(new long[]{1, 2, 3});
        NumbersContainer nc2 = new NumbersContainer(new long[]{1, 2, 3});

        assertEquals(nc1.hashCode(), nc2.hashCode());

        nc1.putLast(4);
        assertNotEquals(nc1.hashCode(), nc2.hashCode());
    }

    @Test
    void testToString() {
        var nc = new NumbersContainer(new long[]{1, 2, 3});
        assertEquals("[1, 2, 3]", nc.toString());
    }

}