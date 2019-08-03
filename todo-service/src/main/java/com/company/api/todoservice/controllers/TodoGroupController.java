package com.company.api.todoservice.controllers;

import com.company.api.todoservice.models.TodoGroupDto;
import com.company.api.todoservice.services.TodoGroupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoGroupController extends BaseController {
    private static final Logger logger = LogManager.getLogger(TodoGroupController.class);

    @Autowired
    private TodoGroupService todoGroupService;

    @PostMapping(value = "/api/todo-group/create")
    public ResponseEntity createTodoGroup(@RequestParam String userId, @RequestBody TodoGroupDto todoGroupDto) {
        logger.info(String.format("Todo group - \'%s\' created for user %s", todoGroupDto.getTodoGroupName(), userId));
        return ResponseEntity.ok(todoGroupService.createTodoGroup(userId, todoGroupDto.getTodoGroupName()));
    }

    @PutMapping(value = "/api/todo-group/update/{id}")
    public ResponseEntity updateTodoGroup(@RequestParam String userId, @RequestBody TodoGroupDto todoGroupDto, @PathVariable Long id) {
        return ResponseEntity.ok(todoGroupService.updateTodoGroup(userId, id, todoGroupDto.getTodoGroupName()));
    }

    @DeleteMapping(value = "/api/todo-group/delete/{id}")
    public ResponseEntity deleteTodoGroup(@RequestParam String userId, @PathVariable Long id) {
        String groupName = todoGroupService.deleteTodoGroup(id, userId);
        return ResponseEntity.ok(String.format("Todo group - %s deleted successfully for user - %s", groupName, userId));
    }

}
