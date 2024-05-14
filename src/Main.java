import model.Status;
import model.Task;
import service.Managers;
import service.TaskManager;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("история: " + taskManager.getHistory());

        int taskId1 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        int taskId2 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task2 = taskManager.getTask(taskId2);
        int taskId3 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task3 = taskManager.getTask(taskId3);
        int taskId4 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task4 = taskManager.getTask(taskId4);
        int taskId5 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task5 = taskManager.getTask(taskId5);
        taskManager.deleteAllTasks();

        System.out.println("история: " + taskManager.getHistory());


    }

}
