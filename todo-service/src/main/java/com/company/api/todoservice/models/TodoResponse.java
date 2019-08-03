package com.company.api.todoservice.models;

import com.company.api.todoservice.domains.Todo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TodoResponse {

    private String userId;
    private List<Todo> todos;

    public static TodoResponse of(String userId, List<Todo> todos) {
        return new TodoResponse(userId, todos);
    }

}
