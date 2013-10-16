package notepad.utils;

import java.util.ArrayDeque;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class BoundedStack<T> {
    private int maxElem;
    private ArrayDeque<T> deque;

    public BoundedStack(int numElements) {
        maxElem = numElements;
        deque = new ArrayDeque<T>(numElements);
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }

    public int size() {
        return deque.size();
    }

    public void push(T t) {
        if (deque.size() == maxElem) {
            deque.pollFirst();
        }
        deque.addLast(t);
    }

    public T peek() {
        return deque.getLast();
    }

    public T pop() {
        return deque.pollLast();
    }

    public void clear() {
        deque.clear();
    }
}
