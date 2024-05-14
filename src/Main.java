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

        /*int epicId1 = taskManager.createEpic(new Epic("3", "3"));
        Epic epic1 = taskManager.getEpic(epicId1);
        System.out.println("пустой эпик:" + epic1);
        System.out.println("история: " + taskManager.getHistory());
        int epicId2 = taskManager.createEpic(new Epic("4", "4"));
        Epic epic2 = taskManager.getEpic(epicId2);
        int subTaskId1 = taskManager.createSubTask(new SubTask("5", "5", Status.NEW, epic1.getTaskId()));
        SubTask subTask1 = taskManager.getSubTask(subTaskId1);
        int subTaskId2 = taskManager.createSubTask(new SubTask("6", "6", Status.DONE, epic2.getTaskId()));
        SubTask subTask2 = taskManager.getSubTask(subTaskId2);
        int subTaskId3 = taskManager.createSubTask(new SubTask("7", "7", Status.NEW, epic2.getTaskId()));
        System.out.println("история: " + taskManager.getHistory());
        SubTask subTask3 = taskManager.getSubTask(subTaskId3);
        System.out.println("список тасок:"+taskManager.getTasks());
        System.out.println("список субтасок:"+taskManager.getSubTasks());
        System.out.println("список эпиков:"+taskManager.getEpics());
        task1.setTaskStatus(Status.DONE);
        System.out.println("таска 1 поменяла статус"+task1);
        subTask3.setTaskStatus(Status.DONE);
        taskManager.updateSubTask(subTask3);
        System.out.println("суб таска поменяла статус"+subTask3);
        System.out.println("эпик поменял статус"+epic2);
        taskManager.deleteTask(task1.getTaskId());
        taskManager.deleteEpic(epic2.getTaskId());
        System.out.println("удалил таску осталась таска 2"+taskManager.getTasks());
        System.out.println("удалили эпик, поэтому удалены суб таски 6 и 7"+taskManager.getSubTasks());
        System.out.println("удалил эпик остался 1"+taskManager.getEpics());
        System.out.println("история: " + taskManager.getHistory());
        Task task =new Task("название","ОПисание", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getTaskId());
        task.setDescription("изменили описание");
        taskManager.getTask(task.getTaskId());
        System.out.println(taskManager.getHistory());*/

    }

}
