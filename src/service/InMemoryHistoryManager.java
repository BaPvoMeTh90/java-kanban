package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }

    }

    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;


    @Override
    public List<Task> getHistory() {
        return getTasks();

    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node current = first;
        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    private void linkLast(Task task) {
        final Node lastSavedNode = last;
        final Node newNode = new Node(lastSavedNode, task, null);
        last = newNode;
        if (lastSavedNode == null) {
            first = newNode;
        } else {
            lastSavedNode.next = newNode;
        }
        history.put(task.getTaskId(), newNode);
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    private void removeNode(Node node) {
        if (node != null) {
            history.remove(node.task.getTaskId());
            Node pervNode = node.prev;
            Node nextNode = node.next;
            if (pervNode == null) {
                first = nextNode;
            } else {
                pervNode.next = nextNode;
                node.prev = null;
            }

            if (nextNode == null) {
                last = pervNode;
            } else {
                nextNode.prev = pervNode;
                node.next = null;
            }
        }
    }

    @Override
    public void historyAdd(Task task) {
        if (task != null) {
            Node node = history.get(task.getTaskId());
            if (node != null) {
                removeNode(node);
            }
            linkLast(task);
        }
    }
}

