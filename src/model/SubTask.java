package model;

public class SubTask extends Task {
     private Epic epic;
     public SubTask(String title, String description, Status taskStatus) {
          super(title, description, taskStatus);
     }
     public Epic getEpic() {
          return epic;
     }

     public void setEpic(Epic epic) {
          this.epic = epic;
     }
}
