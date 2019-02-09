import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int MIN_SIZE = 8;

    private Item[] items;
    private int index;

    public RandomizedQueue() {
        items = (Item[]) new Object[MIN_SIZE];
    }

    private RandomizedQueue(RandomizedQueue<Item> other) {
        this.items = (Item[]) new Object[other.items.length];
        for (int i = 0; i < items.length; ++i) this.items[i] = other.items[i];
        this.index = other.index;
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public int size() {
        return index;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        items[index++] = item;

        checkSizeAfterEnqueue();
    }

    private void checkSizeAfterEnqueue() {
        if (size() != items.length) return;
        Item[] newItems = (Item[]) new Object[items.length * 2];
        for (int i = 0; i < items.length; ++i) newItems[i] = items[i];
        items = newItems;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int randIndex = StdRandom.uniform(0, index);
        Item item = items[randIndex];
        items[randIndex] = items[--index];
        items[index] = null;

        if (items.length > MIN_SIZE && index * 4 <= items.length) {
            Item[] newItems = (Item[]) new Object[items.length / 2];
            for (int i = 0; i < index; ++i) newItems[i] = items[i];
            items = newItems;
        }

        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return items[StdRandom.uniform(0, index)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final RandomizedQueue<Item> copy;

        RandomizedQueueIterator() {
            this.copy = new RandomizedQueue<>(RandomizedQueue.this);
        }

        @Override
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}