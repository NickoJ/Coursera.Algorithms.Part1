import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Deque() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        validateAdd(item);

        Node<Item> newNode = new Node<>(item);
        if (size() == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first = newNode;
            first.next.prev = first;
        }
        ++size;
    }

    public void addLast(Item item) {
        validateAdd(item);

        Node<Item> newNode = new Node<>(item);
        if (size() == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last = newNode;
            last.prev.next = last;
        }
        ++size;
    }

    public Item removeFirst() {
        validateRemove();

        Item item = first.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        --size;
        return item;
    }

    public Item removeLast() {
        validateRemove();

        Item item = last.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        --size;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void validateAdd(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void validateRemove() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    private static class Node<Item> {

        final Item item;
        Node<Item> next;
        Node<Item> prev;

        Node(Item item) {
            this.item = item;
        }

    }

    private class DequeIterator implements Iterator<Item> {

        private Node<Item> node;

        DequeIterator() {
            node = first;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = node.item;
            node = node.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}