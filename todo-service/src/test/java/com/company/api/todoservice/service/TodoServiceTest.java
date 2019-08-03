package com.company.api.todoservice.service;

import com.company.api.todoservice.domains.Todo;
import com.company.api.todoservice.domains.TodoGroup;
import com.company.api.todoservice.exceptions.TodoGroupNotFoundException;
import com.company.api.todoservice.exceptions.TodoNotFoundException;
import com.company.api.todoservice.models.TodoDto;
import com.company.api.todoservice.models.TodoResponse;
import com.company.api.todoservice.repositories.TodoGroupRepository;
import com.company.api.todoservice.repositories.TodoRepository;
import com.company.api.todoservice.services.TodoService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.company.api.todoservice.models.TodoStatus.COMPLETED;
import static com.company.api.todoservice.models.TodoStatus.PENDING;
import static com.company.api.todoservice.utils.DateUtils.safeParseOffsetDateTime;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    private static final String userId = "100";
    private static final String groupName = "Test group";

    private final TodoGroup todoGroup = new TodoGroup(userId, groupName);

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoGroupRepository todoGroupRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        todoGroupRepository.save(todoGroup);
    }

    @Test
    public void ensureNewlyCreatedTodoIsPendingAndNotPriority() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        Todo expectedTodo = new Todo(userId, "Test", todoGroup);

        when(todoRepository.save(expectedTodo)).thenReturn(expectedTodo);

        Todo actualTodo = todoService.createTodo(new TodoDto(userId, "Test", groupName, null));

        assertNotNull(actualTodo);
        assertEquals(PENDING, actualTodo.getTodoStatus());
        assertFalse(actualTodo.isPriority());
    }

    @Test
    public void shouldHaveTodoListSizeAsZeroWhenTodosNotFoundForUser() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        TodoResponse actual = todoService.getTodos(userId, groupName);
        assertNotNull(actual);
        assertEquals(0, actual.getTodos().size());
    }

    @Test
    public void shouldUpdateTodoText() {
        Todo aTodo = new Todo(1L, userId, "Typo test", todoGroup);

        when(todoRepository.save(aTodo)).thenReturn(aTodo);

        TodoGroup todoGroup = new TodoGroup(userId, groupName, singletonList(aTodo));
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        Todo actualTodo = todoService.updateTodo(1L, new TodoDto(userId, "Test", groupName, null));
        assertNotNull(actualTodo);
        assertEquals("Test", actualTodo.getToDoText());

        expectedEx.expect(TodoGroupNotFoundException.class);
        expectedEx.expectMessage(String.format("Todo group name - \'Unknown group\' not found for user - %s", userId));

        todoService.updateTodo(2L, new TodoDto(userId, "Not found", "Unknown group", null));
    }

    @Test
    public void shouldProperlyMarkAsCompleteAndReturnUpdatedTodo() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        TodoResponse todoResponse = todoService.markAsComplete(asList(1L, 2L, 3L), userId, groupName);
        assertNotNull(todoResponse);

        todoResponse.getTodos().forEach(actualTodo -> assertEquals(COMPLETED, actualTodo.getTodoStatus()));
    }

    @Test
    public void shouldMarkAsPriorityTodoAndReturnUpdatedTodo() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        Todo aTodo = new Todo(1L, userId, "Test", todoGroup);

        when(todoRepository.save(aTodo)).thenReturn(aTodo);
        doReturn(Optional.of(aTodo)).when(todoRepository).findById(1L);

        Todo actualTodo = todoService.markAsPriority(1L, userId, groupName);
        assertNotNull(actualTodo);
        assertTrue(actualTodo.isPriority());

        expectedEx.expect(TodoNotFoundException.class);
        expectedEx.expectMessage("Todo 2 not found");

        todoService.markAsPriority(2L, userId, groupName);
    }

    @Test
    public void shouldResetPriorityIfTodoIsCompleted() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        Todo aTodo = new Todo(1L, userId, "Test", todoGroup);

        assertEquals(PENDING, aTodo.getTodoStatus());
        assertTrue(aTodo.isPriority());

        TodoResponse actualTodoResponse = todoService.markAsComplete(singletonList(1L), userId, groupName);
        assertNotNull(actualTodoResponse);
        actualTodoResponse.getTodos().forEach(actualTodo -> {
            assertEquals(COMPLETED, actualTodo.getTodoStatus());
            assertFalse(actualTodo.isPriority());
        });
    }

    @Test
    public void shouldReturnOnlyCompletedTodosForRequestedUser() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        List<Todo> todosForUser100 = asList(
                new Todo(userId, "Test 1", PENDING, safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), todoGroup),
                new Todo(userId, "Test 2", COMPLETED, safeParseOffsetDateTime("2018-01-19T15:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T16:45:56.345Z"), todoGroup),
                new Todo(userId, "Test 3", COMPLETED, safeParseOffsetDateTime("2018-01-19T17:45:56.455Z"), safeParseOffsetDateTime("2018-01-20T16:45:56.345Z"), todoGroup)
        );

        doReturn(Optional.of(todosForUser100))
                .when(todoRepository).findByUserIdAndTodoGroup(userId, todoGroup);

        List<Todo> completedTodos = asList(
                new Todo(userId, "Test 2", COMPLETED, safeParseOffsetDateTime("2018-01-19T15:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T16:45:56.345Z"), todoGroup),
                new Todo(userId, "Test 3", COMPLETED, safeParseOffsetDateTime("2018-01-19T17:45:56.455Z"), safeParseOffsetDateTime("2018-01-20T16:45:56.345Z"), todoGroup)
        );

        TodoResponse actualTodoResponseForUser100 = todoService.completedTodos(userId, groupName);
        assertNotNull(actualTodoResponseForUser100);
        assertEquals(2, actualTodoResponseForUser100.getTodos().size());
        assertEquals(completedTodos, actualTodoResponseForUser100.getTodos());

        expectedEx.expect(TodoGroupNotFoundException.class);
        expectedEx.expectMessage("Todo group name - \'Unknown group\' not found for user - 102");

        todoService.completedTodos("102", "Unknown group");

    }

    @Test
    public void shouldReturnOnlyPriorityTodosForRequestedUser() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        List<Todo> todosForUser100 = asList(
                new Todo(userId, "Test 1", PENDING, true, safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), todoGroup),
                new Todo(userId, "Test 2", PENDING, true, safeParseOffsetDateTime("2018-01-11T14:45:56.555Z"), safeParseOffsetDateTime("2018-01-18T14:45:56.555Z"), todoGroup),
                new Todo(userId, "Test 3", COMPLETED, false, safeParseOffsetDateTime("2018-01-19T15:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T16:45:56.345Z"), todoGroup)
        );

        doReturn(Optional.of(todosForUser100)).when(todoRepository).findByUserIdAndTodoGroup(userId, todoGroup);

        List<Todo> priorityTodos = asList(
                new Todo(userId, "Test 2", PENDING, true, safeParseOffsetDateTime("2018-01-11T14:45:56.555Z"), safeParseOffsetDateTime("2018-01-18T14:45:56.555Z"), todoGroup),
                new Todo(userId, "Test 1", PENDING, true, safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), safeParseOffsetDateTime("2018-01-19T14:45:56.555Z"), todoGroup)
        );

        TodoResponse actualTodoResponseForUser100 = todoService.priorityTodos(userId, groupName);
        assertNotNull(actualTodoResponseForUser100);
        assertEquals(2, actualTodoResponseForUser100.getTodos().size());
        assertEquals(priorityTodos, actualTodoResponseForUser100.getTodos());

        expectedEx.expect(TodoGroupNotFoundException.class);
        expectedEx.expectMessage("Todo group name - \'Unknown group\' not found for user - 102");

        todoService.priorityTodos("102", "Unknown group");
    }

    @Test
    public void shouldProperlyDeleteATodo() {
        when(todoGroupRepository.findByGroupNameAndUserId(groupName, userId))
                .thenReturn(Optional.of(todoGroup));

        when(todoRepository.deleteByUserIdAndId(userId, 3L)).thenReturn(1);

        todoService.deleteTodo(userId, groupName, 3L);

        expectedEx.expect(TodoGroupNotFoundException.class);
        expectedEx.expectMessage("Todo group name - \'Unknown group\' not found for user - 102");

        todoService.deleteTodo("102", "Unknown group", 1L);
    }

}
