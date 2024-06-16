import service.FileBackedTasksManager;
import service.TaskManager;

import java.io.File;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File("resources", "toLoad.csv"));


    }

}
