package com.todo.task;

import java.time.LocalDate;

public record TaskRegistrationRequest(
   LocalDate dueDate,
   LocalDate startDate,
   String taskName,
   String topic,
   String description,
   String priority,
   Integer user_id
){}
