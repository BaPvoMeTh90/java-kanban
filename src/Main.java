import model.Epic;
import model.SubTask;
import service.TaskManager;
import model.Status;
import model.Task;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("1","1", Status.NEW));
        taskManager.createTask(new Task("2","2", Status.NEW));
        taskManager.createEpic(new Epic("3","3"));
        taskManager.createEpic(new Epic("4","4"));
        Epic epic1 = taskManager.getEpic(3);
        Epic epic2 = taskManager.getEpic(4);
        taskManager.createSubTask(new SubTask("5","5",Status.NEW,epic1));
        taskManager.createSubTask(new SubTask("6","6",Status.DONE,epic2));
        taskManager.createSubTask(new SubTask("7","7",Status.NEW,epic2));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());
        Task task1 = taskManager.getTask(1);
        task1.setTaskStatus(Status.DONE);
        System.out.println(task1);
        SubTask subTask1 = taskManager.getSubTask(7);
        subTask1.setTaskStatus(Status.DONE);
        System.out.println(subTask1);
        System.out.println(epic2);
        taskManager.deleteTask(1);
        taskManager.deleteEpic(3);


    }

}
