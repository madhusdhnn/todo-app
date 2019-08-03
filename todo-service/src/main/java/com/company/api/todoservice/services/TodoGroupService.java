package com.company.api.todoservice.services;

import com.company.api.todoservice.domains.TodoGroup;
import com.company.api.todoservice.exceptions.TodoGroupNotFoundException;
import com.company.api.todoservice.repositories.TodoGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TodoGroupService {

    @Autowired
    private TodoGroupRepository todoGroupRepository;

    public TodoGroup createTodoGroup(String userId, String todoGroup) {
        return todoGroupRepository.save(new TodoGroup(userId, todoGroup));
    }

    public TodoGroup updateTodoGroup(String userId, Long groupId, String newGroupName) {
        return withTodoGroup(userId, groupId, todoGroup -> {
            todoGroup.setGroupName(newGroupName);
            return todoGroupRepository.save(todoGroup);
        });
    }

    public String deleteTodoGroup(Long groupId, String userId) {
        return withTodoGroup(userId, groupId, todoGroup -> {
            String groupName = todoGroup.getGroupName();
            todoGroupRepository.delete(todoGroup);
            return groupName;
        });
    }

    private <R> R withTodoGroup(String userId, Long groupId, Function<TodoGroup, R> handler) {
        TodoGroup todoGroup = todoGroupRepository.findByIdAndUserIdEquals(groupId, userId)
                .orElseThrow(() -> new TodoGroupNotFoundException(String.format("Todo group not found for user - %s", userId)));

        return handler.apply(todoGroup);
    }

}
