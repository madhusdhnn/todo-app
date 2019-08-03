package com.company.api.todoservice.services;

import com.company.api.todoservice.domains.Todo;
import com.company.api.todoservice.domains.TodoGroup;
import com.company.api.todoservice.exceptions.TodoGroupNotFoundException;
import com.company.api.todoservice.exceptions.TodoNotFoundException;
import com.company.api.todoservice.models.TodoDto;
import com.company.api.todoservice.models.TodoResponse;
import com.company.api.todoservice.repositories.TodoGroupRepository;
import com.company.api.todoservice.repositories.TodoRepository;
import com.company.api.todoservice.utils.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static com.company.api.todoservice.models.TodoStatus.COMPLETED;
import static com.diffplug.common.base.Errors.rethrow;
import static java.util.stream.Collectors.toList;

@Service
public class TodoService {
    private static final Logger logger = LogManager.getLogger(TodoService.class);

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoGroupRepository todoGroupRepository;

    public Todo createTodo(TodoDto todoDto) {
        String todoGroupName = todoDto.getTodoGroupName();
        String userId = todoDto.getUserId();

        return withTodoGroup(todoGroupName, userId, todoGroup -> todoRepository.save(new Todo(userId, todoDto.getTodoText(), todoGroup)));
    }

    public TodoResponse getTodos(String userId, String groupName) {
        return withTodoGroup(groupName, userId, todoGroup -> {
            List<Todo> todos = todoGroup.getTodos();
            return TodoResponse.of(userId, CollectionUtils.sortList(todos));
        });
    }

    public Todo updateTodo(Long id, TodoDto todoDto) {
        Todo aTodo = withTodoGroup(todoDto.getTodoGroupName(), todoDto.getUserId(), todoGroup -> todoGroup.getTodos().stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException(String.format("Todo %s not found", todoDto.getUserId())))
        );

        aTodo.setTodoText(todoDto.getTodoText());

        return todoRepository.save(aTodo);
    }

    public TodoResponse markAsComplete(List<Long> todoIds, String userId, String groupName) {
        List<Todo> completedTodos = withTodoGroup(groupName, userId,
                todoGroup -> todoGroup.getTodos().stream()
                        .filter(todo -> todoIds.contains(todo.getId()))
                        .map(Todo::markAsComplete)
                        .collect(toList()));

        if (completedTodos.isEmpty())
            return TodoResponse.of(userId, completedTodos);
        else {
            todoRepository.saveAll(completedTodos);
            logger.info(String.format("Marking the todo ids - %s, under group - \'%s\', as completed for user - %s", completedTodos, groupName, userId));
            return TodoResponse.of(userId, CollectionUtils.sortList(completedTodos));
        }
    }

    public Todo markAsPriority(Long id, String userId, String groupName) {
        return withTodoGroup(groupName, userId, todoGroup -> withTodo(id, todo -> todoRepository.save(todo.markAsPriority())));
    }

    public void deleteTodo(String userId, String groupName, Long todoId) {
        withTodoGroup(groupName, userId, rethrow().wrapFunction(todoGroup -> {
            int affectedRows = todoRepository.deleteByUserIdAndId(userId, todoId);
            if (affectedRows == 0) {
                throw new TodoNotFoundException(String.format("Todo %s not found for user %s", todoId, userId));
            }
            return null;
        }));
    }

    public TodoResponse completedTodos(String userId, String groupName) {
        return withTodoGroup(groupName, userId, rethrow().wrapFunction(todoGroup -> {
            List<Todo> completedTodos = getCompletedTodos(userId, todoGroup);
            return TodoResponse.of(userId, CollectionUtils.sortList(completedTodos));
        }));
    }

    public TodoResponse priorityTodos(String userId, String groupName) {
        return withTodoGroup(groupName, userId, rethrow().wrapFunction(todoGroup -> {
            List<Todo> priorityTodos = getPriorityTodos(userId, todoGroup);
            return TodoResponse.of(userId, CollectionUtils.sortList(priorityTodos));
        }));
    }

    private List<Todo> getCompletedTodos(String userId, TodoGroup todoGroup) {
        List<Todo> todos = todoRepository.findByUserIdAndTodoGroup(userId, todoGroup)
                .orElseThrow(() -> new TodoNotFoundException(String.format("Todos not found for user - %s", userId)));

        return todos.stream()
                .filter(todo -> todo.getTodoStatus().equals(COMPLETED))
                .collect(toList());
    }

    private List<Todo> getPriorityTodos(String userId, TodoGroup todoGroup) {
        List<Todo> todos = todoRepository.findByUserIdAndTodoGroup(userId, todoGroup)
                .orElseThrow(() -> new TodoNotFoundException(String.format("Todos not found for user - %s", userId)));

        return todos.stream()
                .filter(todo -> !todo.isAlreadyCompleted())
                .filter(Todo::isPriority)
                .collect(toList());
    }

    private <R> R withTodoGroup(String todoGroupName, String userId, Function<TodoGroup, R> handler) {
        TodoGroup todoGroup = todoGroupRepository.findByGroupNameAndUserId(todoGroupName, userId)
                .orElseThrow(() -> new TodoGroupNotFoundException(String.format("Todo group name - \'%s\' not found for user - %s", todoGroupName, userId)));

        return handler.apply(todoGroup);
    }

    private <R> R withTodo(Long id, Function<Todo, R> handler) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(String.format("Todo %s not found", id)));

        return handler.apply(todo);
    }

}
