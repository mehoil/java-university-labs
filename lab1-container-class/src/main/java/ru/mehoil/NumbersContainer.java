package ru.mehoil;

/**
 * Encapsulates an array of type long and provides
 * methods for interacting with it.
 *
 * @author Mikhail Dorokhov
 * @since 1.0
 */
public class NumbersContainer {

    private static final int INITIAL_SIZE = 128;
    private static final float RESIZING_FACTOR = 1.5f;

    private long[] numbers = new long[INITIAL_SIZE];
    private int maxSize = INITIAL_SIZE;
    private int currentSize = 0;

    public NumbersContainer(final long[] numbers) {
        putArray(numbers);
    }

    public NumbersContainer() {
    }

    public void put(final int index, final long number) {
        validateIndex(index);
        if (index == currentSize) {
            putLast(number);
        } else {
            shiftRightAt(index);
            numbers[index] = number;
        }
    }

    public void putFirst(final long number) {
        shiftRightAt(0);
        numbers[0] = number;
    }

    public void putLast(final long number) {
        if (currentSize + 1 > maxSize) {
            resize();
        }
        numbers[currentSize++] = number;
    }

    public void putAll(final long[] numbers) {
        putArray(numbers);
    }

    public void remove(final int index) {
        validateIndex(index);
        shiftLeftAt(index);
    }

    public void removeFirst() {
        shiftLeftAt(0);
    }

    public void removeLast() {
        if (currentSize < 1) {
            throw new IllegalArgumentException("The array is empty");
        }
        numbers[--currentSize] = 0;
    }

    public long get(final int index) {
        validateIndex(index);
        return numbers[index];
    }

    public long getFirst() {
        return numbers[0];
    }

    public long getLast() {
        return numbers[currentSize - 1];
    }

    public int getSize() {
        return currentSize;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void clear() {
        currentSize = 0;
        maxSize = INITIAL_SIZE;
        numbers = new long[maxSize];
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof final NumbersContainer other)) {
            return false;
        }

        if (currentSize != other.currentSize) {
            return false;
        }
        for (int i = 0; i < currentSize; i++) {
            if (numbers[i] != other.numbers[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = currentSize;
        for (int i = 0; i < currentSize; i++) {
            result = 31 * result + Long.hashCode(numbers[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        final var sb = new StringBuilder("[");
        for (int i = 0; i < currentSize - 1; i++) {
            sb.append(numbers[i]);
            sb.append(", ");
        }
        sb.append(numbers[currentSize - 1]);
        sb.append("]");

        return sb.toString();
    }

    private void shiftLeftAt(final int index) {
        validateIndex(index);
        if (currentSize - 1 < 0) {
            throw new IllegalArgumentException("Cannot shift array to the left if it's empty");
        }

        for (int i = index; i < currentSize - 1; i++) {
            numbers[i] = numbers[i + 1];
        }
        currentSize--;
    }

    private void shiftRightAt(final int index) {
        validateIndex(index);
        if (currentSize + 1 > maxSize) {
            resize();
        }

        for (int i = currentSize; i > index; i--) {
            numbers[i] = numbers[i - 1];
        }
        currentSize++;

        numbers[index] = 0;
    }

    private void putArray(final long[] numbers) {
        final int newNumbersSize = numbers.length;
        final int requiredSize = currentSize + newNumbersSize;

        if (requiredSize > maxSize) {
            final int newSize = (int) Math.min(
                    requiredSize * RESIZING_FACTOR,
                    Integer.MAX_VALUE
            );
            resize(newSize);
        }
        for (int i = 0; i < newNumbersSize; i++) {
            this.numbers[currentSize + i] = numbers[i];
        }

        currentSize += newNumbersSize;
    }

    private void resize(final int newSize) {
        final long[] newNumbers = new long[newSize];
        for (int i = 0; i < currentSize && i < newSize; i++) {
            newNumbers[i] = numbers[i];
        }

        currentSize = Math.min(currentSize, newSize);
        numbers = newNumbers;
        maxSize = newSize;
    }

    private void resize() {
        if (currentSize == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The array size exceeds its maximum size");
        }
        final int newSize = (int) (currentSize * RESIZING_FACTOR);
        resize(newSize);
    }

    private void validateIndex(final int index) {
        if (index < 0 || index > currentSize) {
            throw new IllegalArgumentException("Index is not within bounds");
        }
    }
}
