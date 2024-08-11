import model.Status;
import model.Task;
import service.FileBackedTasksManager;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import service.http.HttpTaskServer;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        File file = new File("resources", "toLoad.csv");

        TaskManager tm = Managers.getDefault();
        //TaskManager ltm = new FileBackedTasksManager(Managers.getDefaultHistory(), file);

        System.out.println(new Task("","", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0)).toString());
        System.out.println(HttpTaskServer.getGson().toJson(new Task("","", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0))));
        //Task task =
    }


}
