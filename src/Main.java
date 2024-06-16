import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;


public class Main {

    public static void main(String[] args) {

        //TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File("resources", "toLoad.csv"));


        TaskManager taskManager = Managers.getDefault();

        System.out.println("история: " + taskManager.getHistory());

        int taskId1 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        int taskId2 = taskManager.createEpic(new Epic(" ", " "));
        Epic task2 = taskManager.getEpic(taskId2);
        int taskId3 = taskManager.createSubTask(new SubTask("", "", Status.NEW, 2));
        SubTask task3 = taskManager.getSubTask(taskId3);


        System.out.println("1" + taskManager.getTasks().toString() + "\n");
        System.out.println("2" + taskManager.getSubTasks().toString() + "\n");
        System.out.println("3" + taskManager.getEpics().toString() + "\n");

        /*Epic epic = taskManager.getEpic(taskId2);
        System.out.println("Пусто?"+ epic.getSubTasks().toString());*/

    }

}
