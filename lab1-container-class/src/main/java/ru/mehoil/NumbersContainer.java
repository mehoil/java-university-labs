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
        if (currentSize + 1 > maxSize) {
            resize();
        }

        shiftRightAt(index);
        numbers[index] = number;
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

    public boolean isEmpty() {
        return currentSize == 0;
    }


    private void shiftLeftAt(final int index) {
        validateIndex(index);
        if (currentSize - 1 < 0) {
            throw new IllegalArgumentException("Cannot shift array to the left if it's empty");
        }

        currentSize--;
        for (int i = index; i < currentSize - 1; i++) {
            numbers[i] = numbers[i + 1];
        }
        numbers[currentSize - 1] = 0;
    }

    private void shiftRightAt(final int index) {
        validateIndex(index);
        if (currentSize + 1 > maxSize) {
            resize();
        }

        currentSize++;
        for (int i = currentSize - 1; i > index; i--) {
            numbers[i] = numbers[i - 1];
        }
        numbers[index] = 0;
    }

    private void putArray(final long[] numbers) {
        final int estimatedSize = this.currentSize + numbers.length;
        if (estimatedSize > maxSize) {
            final long totalSum = this.numbers.length + numbers.length;
            final long totalSize = (long) (totalSum * RESIZING_FACTOR);

            if (totalSum >= Integer.MAX_VALUE) {
                throw new IllegalArgumentException("The array size exceeds its maximum size");
            }

            if (totalSize > Integer.MAX_VALUE) {
                resize(Integer.MAX_VALUE);
            } else {
                resize((int) totalSize);
            }
        }

        for (int i = currentSize; i < numbers.length; i++) {
            this.numbers[i] = numbers[i];
        }
        currentSize = currentSize + numbers.length;
    }

    private void resize(final int newSize) {
        final long[] oldNumbers = this.numbers;
        this.numbers = new long[newSize];
        this.maxSize = newSize;

        for (int i = 0; i < maxSize; i++) {
            this.numbers[i] = oldNumbers[i];
        }
    }

    private void resize() {
        if (currentSize == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The array size exceeds its maximum size");
        }
        final int newSize = (int) (currentSize * RESIZING_FACTOR);
        resize(newSize);
    }

    private void validateIndex(final int index) {
        if (index < 0 || index >= currentSize) {
            throw new IllegalArgumentException("Index is not within bounds");
        }
    }
}
