package com.todo.task.subtask;

public class SubTask {

    String taskName;
    String description;
    Integer subtask_id;
    Integer parentTaskId;

    public SubTask(){}

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSubtask_id() {
        return subtask_id;
    }

    public void setSubtask_id(Integer subtask_id) {
        this.subtask_id = subtask_id;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((subtask_id == null) ? 0 : subtask_id.hashCode());
        result = prime * result + ((parentTaskId == null) ? 0 : parentTaskId.hashCode());
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
        SubTask other = (SubTask) obj;
        if (taskName == null) {
            if (other.taskName != null)
                return false;
        } else if (!taskName.equals(other.taskName))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (subtask_id == null) {
            if (other.subtask_id != null)
                return false;
        } else if (!subtask_id.equals(other.subtask_id))
            return false;
        if (parentTaskId == null) {
            if (other.parentTaskId != null)
                return false;
        } else if (!parentTaskId.equals(other.parentTaskId))
            return false;
        return true;
    }

    
}
