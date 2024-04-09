import cervice.TaskManager;
import model.Status;
import model.Task;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("первая", "первая описана", Status.NEW));



    }

}
