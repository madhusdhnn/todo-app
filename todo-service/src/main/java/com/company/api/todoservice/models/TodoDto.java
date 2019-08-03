package com.company.api.todoservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodoDto {

    private String userId;
    private String todoText;
    private String todoGroupName;
    private List<Long> todoIds;

    public TodoDto(String userId, TodoDto other) {
        this.userId = userId;
        this.todoText = other.todoText;
        this.todoGroupName = other.todoGroupName;
    }

}
