import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size, n;

    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;

        public ArrayIterator() {
            StdRandom.shuffle(queue, 0, n);

            i = 0;
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return queue[i++];
        }
    }

    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        size = 2;
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int newSize) {
        Item[] newQueue = (Item[]) new Object[newSize];

        for (int i = 0; i < n; i++) {
            newQueue[i] = queue[i];
        }

        queue = newQueue;
        size = newSize;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (n == size) {
            resize(size * 2);
        }

        queue[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (n == size / 4) {
            resize(size / 2);
        }

        int rand = StdRandom.uniform(n--);
        Item item = queue[rand];

        if (rand != n) queue[rand] = queue[n];
        queue[n] = null;

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return queue[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> random = new RandomizedQueue<Integer>();

        random.enqueue(1);
        random.enqueue(0);
        random.enqueue(1);
        random.enqueue(2);

        StdOut.println(random.size());
        StdOut.println(random.sample());

        for (Integer i : random) {
            StdOut.println(i);
        }

        StdOut.println(random.dequeue());
        StdOut.println(random.dequeue());
        StdOut.println(random.dequeue());
        StdOut.println(random.dequeue());

        StdOut.println(random.isEmpty());
    }
}