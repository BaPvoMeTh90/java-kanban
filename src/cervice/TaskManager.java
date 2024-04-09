package cervice;
import model.SubTask;
import model.Task;
import model.Epic;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer,SubTask> subTasks = new HashMap<>();
    private HashMap<Integer,Epic> epics = new HashMap<>();
    int counter = 1;
    private int generateId(){
        return ++counter;
    }

    public Task getTask(int id){
          return tasks.get(id);
    }
    public SubTask getSubTask(int id) {
        return subTasks.get(id);

    }
    public Epic getEpic(int id) {
         return epics.get(id);
    }

    public void deleteTask(int id){
       tasks.remove(id);
    }
    public void deleteSubTask(int id){
        SubTask subTaskToRemove = subTasks.remove(id);
        Epic epicToChange = subTaskToRemove.getEpic();
        ArrayList<SubTask> listOfEpicToChange = epicToChange.getSubTasks();
        listOfEpicToChange.remove(subTaskToRemove);
        epicToChange.updateStatus();
    }
    public void deleteEpic(int id){
        Epic epicToRemove = epics.remove(id);
        ArrayList<SubTask> subTasksToDelete = epicToRemove.getSubTasks();
        for(int i =1; i<= subTasksToDelete.size(); i++){
            subTasks.remove(subTasksToDelete.get(i).getTaskId());
        }
    }


    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public Task createTask(Task task){
    task.setTaskId(generateId());
    tasks.put(task.getTaskId(),task);
    return task;
    }
    public SubTask createSubTask(SubTask subTask){
        subTask.setTaskId(generateId());
        subTasks.put(subTask.getTaskId(), subTask);
        Epic epic = epics.get(subTask.getEpic().getTaskId());
        epic.addSubTask(subTask);
        epic.updateStatus();
        return subTask;
    }

    public Epic createEpic(Epic epic){
         epic.setTaskId(generateId());
         epics.put(epic.getTaskId(), epic);                                                                             //
        return epic;
    }

    public void deleteAllTasks(){
        tasks.clear();
    }
    public void deleteAllSubTasks(){
        subTasks.clear();
        epics.clear();
    }
    public void deleteAllEpic(){
        epics.clear();
        subTasks.clear();
    }

    public void upToDateTask(Task task){
        tasks.remove(task.getTaskId());
        tasks.put(task.getTaskId(),task);
    }
    public void upToDateSubTask(SubTask subTask){
        subTasks.remove(subTask.getTaskId());
        subTasks.put(subTask.getTaskId(), subTask);
    }
    public void upToDateEpic(Epic epic){
        epics.remove(epic.getTaskId());
        epics.put(epic.getTaskId(), epic);
    }
}