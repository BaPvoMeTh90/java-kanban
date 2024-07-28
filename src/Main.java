import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0)));
        int epic = manager.createEpic(new Epic("", ""));
        manager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0), epic));
        manager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(0), epic));
        manager.createSubTask(new SubTask("", "", Status.DONE, LocalDateTime.now(), Duration.ofMinutes(0), epic));


        System.out.println(manager.getEpic(epic).getTaskStatus());

//        System.out.println("субы "+manager.getSubTasks());
//        System.out.println("приоритеты" + manager.getPrioritizedTasks());
//        TaskManager loader = FileBackedTasksManager.loadFromFile(new File("resources", "toLoad.csv"));
//
//        System.out.println(loader.getSubTasks());
//        System.out.println(loader.getPrioritizedTasks());
    }

}
