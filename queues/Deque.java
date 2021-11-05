import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first, last;
    private int size;

    private class Node<Item> {
        Item item;
        Node<Item> next, prev;
    }

    private class LinkedListIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedListIterator() {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Deque() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.prev = null;
        
        if (oldFirst != null)
            first.next = oldFirst;

        if (isEmpty()) last = first;
        else oldFirst.prev = first;

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;

        if (oldLast != null)
            last.prev = oldLast;

        if (isEmpty()) first = last;
        else oldLast.next = last;

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item oldFirstItem = first.item;
        first = first.next;

        size--;

        if (!isEmpty()) first.prev = null;

        return oldFirstItem;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item oldLastItem = last.item;
        last = last.prev;

        size--;

        if (!isEmpty()) last.next = null;

        return oldLastItem;
    }

    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();

        deque.addLast(0);
        deque.addFirst(1);
        deque.addLast(1);
        deque.addLast(2);

        StdOut.println(deque.size());

        for (Integer i : deque) {
            StdOut.println(i);
        }

        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeFirst());

        StdOut.println(deque.isEmpty());
    }
}