package managers;

import tasks.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node node = head;
        while (Objects.nonNull(node)) {
            result.add(node.getData());
            node = node.getNext();
        }
        return List.copyOf(result);
    }

    @Override
    public void add(Task task) {
        if (Objects.nonNull(task)) {
            int id = task.getId();
            if (historyMap.containsKey(id)) {
                remove(id);
            }
            Node current;
            if (head == null) {
                current = new Node(task);
                head = current;
                tail = current;
            } else {
                current = new Node(task);
                current.setPrevious(tail);
                tail.setNext(current);
                tail = current;
            }
            historyMap.put(id, current);
        }
    }

    @Override
    public void remove(int id) {
        Node current = historyMap.get(id);
        removeNode(current);
        historyMap.remove(id);
    }

    private void removeNode(Node current) {
        if (Objects.nonNull(current)) {
            Node prevN;
            Node nextN;
            if (current.equals(head)) {
                if (current.getNext() == null) {
                    return;
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
    public boolean isKey(int key) {
        return historyMap.containsKey(key);
    }

    private class Node {
        Node next;
        Node previous;
        Task data;

        public Node(Task data) {
            this.data = data;
            this.previous = null;
            this.next = null;
        }

        public Task getData() {
            return data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public Node getPrevious() {
            return previous;
        }
    }
}