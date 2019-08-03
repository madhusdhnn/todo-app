package com.company.api.todoservice.controllers;

import com.company.api.todoservice.domains.Todo;
import com.company.api.todoservice.models.TodoDto;
import com.company.api.todoservice.models.TodoResponse;
import com.company.api.todoservice.services.TodoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TodoController extends BaseController {
    private static final Logger logger = LogManager.getLogger(TodoController.class);

    @Autowired
    private TodoService toDoService;

    @PostMapping(value = "/api/todo/create")
    public Todo createNewTodo(@RequestParam("userId") String userId, @RequestBody TodoDto todoDto) {
        return toDoService.createTodo(new TodoDto(userId, todoDto));
    }

    @GetMapping(value = "/api/todo/todos")
    public TodoResponse getAllTodos(@RequestParam("userId") String userId, @RequestParam("groupName") String groupName) {
        return toDoService.getTodos(userId, groupName);
    }

    @GetMapping(value = "/api/todo/todos/completed")
    public TodoResponse completedTodos(@RequestParam("userId") String userId, @RequestParam("groupName") String groupName) {
        return toDoService.completedTodos(userId, groupName);
    }

    @GetMapping(value = "/api/todo/todos/priority")
    public TodoResponse priorityTodos(@RequestParam("userId") String userId, @RequestParam("groupName") String groupName) {
        return toDoService.priorityTodos(userId, groupName);
    }

    @PutMapping(value = "/api/todo/update/{id}")
    public Todo updateTodo(@RequestParam("userId") String userId, @PathVariable Long id, @RequestBody TodoDto todoDto) {
        logger.info(String.format("Updating the todo id - %s for user - %s", id, userId));
        return toDoService.updateTodo(id, new TodoDto(userId, todoDto));
    }

    @PutMapping(value = "/api/todo/update/status")
    public TodoResponse markAsComplete(@RequestParam("userId") String userId, @RequestParam("groupName") String groupName, @RequestBody TodoDto todoDto) {
        return toDoService.markAsComplete(todoDto.getTodoIds(), userId, groupName);
    }

    @PutMapping(value = "/api/todo/update/priority/{id}")
    public Todo markAsPriority(@RequestParam("userId") String userId, @PathVariable Long id, @RequestParam("groupName") String groupName) {
        logger.info(String.format("Marking the todo id - %s under group - %s as priority for user - %s", id, groupName, userId));
        return toDoService.markAsPriority(id, userId, groupName);
    }

    @DeleteMapping(value = "/api/todo/delete/{id}")
    public Map<String, Object> deleteTodo(@RequestParam("userId") String userId, @RequestParam("groupName") String groupName, @PathVariable Long id) {
        toDoService.deleteTodo(userId, groupName, id);
        String message = String.format("Todo - %d under group - %s deleted successfully for user - %s", id, groupName, userId);
        logger.info(message);
        return new HashMap<String, Object>() {{
            put("id", id);
            put("message", message);
        }};
    }

}
