package com.todo.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.todo.Priority;
import com.todo.task.subtask.SubTask;

public class Task {
   private LocalDate dueDate;
   private LocalDate startDate;
   private String taskName;
   private String topic;
   private Integer taskId;
   private String description;
   private String priority;
   private List<SubTask> subTasks;
   private Integer user_id;

   public Task()
   {
    this.subTasks=new ArrayList<>();
    }
   
    
    public Task( Integer taskId,LocalDate dueDate, LocalDate startDate, String taskName, String topic, String description, Integer user_id) {
        this.taskId=taskId;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.taskName = taskName;
        this.topic = topic;
        this.description=description;
        this.user_id=user_id;
        this.priority=Priority.Low.toString();
        this.subTasks=new ArrayList<>();
    }

     public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }


    public void addSubtask(SubTask sub)
    {
        subTasks.add(sub);
    }

    public Integer getTaskId() {
    return taskId;
    }

public void setTaskId(Integer taskId) {
    this.taskId = taskId;
    }

public String getDescription() {
    return description;
    }

public void setDescription(String description) {
    this.description = description;
    }

public void setSubTasks(List<SubTask> subTasks) {
    this.subTasks = subTasks;
    }

public String getPriority() {
    return priority;
}

public void setPriority(String priority) {
    this.priority = priority;
}

public Integer getUser_id() {
    return user_id;
}

public void setUser_id(Integer user_Id) {
    this.user_id = user_Id;
}


@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
    result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
    result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
    result = prime * result + ((topic == null) ? 0 : topic.hashCode());
    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((priority == null) ? 0 : priority.hashCode());
    result = prime * result + ((subTasks == null) ? 0 : subTasks.hashCode());
    result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
    return result;
}


@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Task other = (Task) obj;
    if (dueDate == null) {
        if (other.dueDate != null)
            return false;
    } else if (!dueDate.equals(other.dueDate))
        return false;
    if (startDate == null) {
        if (other.startDate != null)
            return false;
    } else if (!startDate.equals(other.startDate))
        return false;
    if (taskName == null) {
        if (other.taskName != null)
            return false;
    } else if (!taskName.equals(other.taskName))
        return false;
    if (topic == null) {
        if (other.topic != null)
            return false;
    } else if (!topic.equals(other.topic))
        return false;
    if (taskId == null) {
        if (other.taskId != null)
            return false;
    } else if (!taskId.equals(other.taskId))
        return false;
    if (description == null) {
        if (other.description != null)
            return false;
    } else if (!description.equals(other.description))
        return false;
    if (priority == null) {
        if (other.priority != null)
            return false;
    } else if (!priority.equals(other.priority))
        return false;
    if (subTasks == null) {
        if (other.subTasks != null)
            return false;
    } else if (!subTasks.equals(other.subTasks))
        return false;
    if (user_id == null) {
        if (other.user_id != null)
            return false;
    } else if (!user_id.equals(other.user_id))
        return false;
    return true;
}


}
