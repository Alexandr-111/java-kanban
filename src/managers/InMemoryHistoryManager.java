package managers;

import tasks.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node<Task> node = head;
        while (Objects.nonNull(node)) {
            result.add(node.getData());
            node = node.getNext();
        }
        return List.copyOf(result);
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> current = null;
        if (Objects.nonNull(task)) {
            if (head == null) {
                current = new Node<>(task);
                head = current;
            } else {
                current = new Node<>(task);
                current.setPrevious(tail);
                tail.setNext(current);
            }
            tail = current;
        }
        return current;
    }

    @Override
    public void add(Task task) {
        if (Objects.nonNull(task)) {
            int id = task.getId();
            if (historyMap.containsKey(id)) {
                remove(id);
            }
            historyMap.put(id, linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        Node<Task> current = historyMap.get(id);
        removeNode(current);
        historyMap.remove(id);
    }

    private void removeNode(Node<Task> current) {
        if (Objects.nonNull(current)) {
            Node<Task> prevN;
            Node<Task> nextN;
            if (current.equals(head)) {
                if (current.getNext() == null) {
                    head = null;
                    tail = null;
                } else {
                    nextN = current.getNext();
                    nextN.setPrevious(null);
                    head = nextN;
                }
            } else if (current.equals(tail)) {
                prevN = current.getPrevious();
                prevN.setNext(null);
                tail = prevN;
            } else {
                prevN = current.getPrevious();
                nextN = current.getNext();
                nextN.setPrevious(prevN);
                prevN.setNext(nextN);
            }
        }
    }

    @Override
    public boolean existInBrowsingHistory(int key) {
        return historyMap.containsKey(key);
    }

    private static class Node<T> {
        Node<T> next;
        Node<T> previous;
        T data;

        public Node(T data) {
            this.data = data;
            this.previous = null;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setPrevious(Node<T> previous) {
            this.previous = previous;
        }

        public Node<T> getPrevious() {
            return previous;
        }
    }
}