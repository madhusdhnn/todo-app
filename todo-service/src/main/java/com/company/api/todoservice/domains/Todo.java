package com.company.api.todoservice.domains;

import com.company.api.todoservice.models.TodoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static com.company.api.todoservice.models.TodoStatus.COMPLETED;
import static com.company.api.todoservice.models.TodoStatus.PENDING;

@Entity
@Table(name = "todo")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Todo implements Comparable<Todo> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "todo_text")
    @JsonProperty(value = "text")
    private String toDoText;

    @Column(name = "todo_status")
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private TodoStatus todoStatus;

    @Column(name = "is_priority")
    private boolean isPriority;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private TodoGroup todoGroup;

    public Todo(String userId, String toDoText, TodoGroup todoGroup) {
        this.userId = userId;
        this.toDoText = toDoText;
        this.todoStatus = PENDING;
        this.isPriority = false;
        this.todoGroup = todoGroup;
    }

    public Todo(Long id, String userId, String todoText, TodoGroup todoGroup) {
        this.id = id;
        this.userId = userId;
        this.toDoText = todoText;
        this.todoStatus = PENDING;
        this.isPriority = true;
        this.todoGroup = todoGroup;
    }

    public Todo(String userId, String todoText, TodoStatus todoStatus, OffsetDateTime createdAt, OffsetDateTime updatedAt, TodoGroup todoGroup) {
        this.userId = userId;
        this.toDoText = todoText;
        this.todoStatus = todoStatus;
        this.isPriority = true;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.todoGroup = todoGroup;
    }

    public Todo(String userId, String todoText, TodoStatus todoStatus, boolean isPriority, OffsetDateTime createdAt, OffsetDateTime updatedAt, TodoGroup todoGroup) {
        this.userId = userId;
        this.toDoText = todoText;
        this.todoStatus = todoStatus;
        this.isPriority = isPriority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.todoGroup = todoGroup;
    }

    public void setTodoText(String todoText) {
        if (todoText != null)
            this.toDoText = todoText;
    }

    public Todo markAsComplete() {
        this.todoStatus = COMPLETED;
        resetPriority();
        return this;
    }

    private void resetPriority() {
        this.isPriority = false;
    }

    public Todo markAsPriority() {
        if (!this.isPriority)
            this.isPriority = true;
        return this;
    }

    @JsonIgnore
    public boolean isAlreadyCompleted() {
        return this.todoStatus.equals(COMPLETED);
    }

    @Override
    public int compareTo(Todo other) {
        return this.updatedAt.compareTo(other.getUpdatedAt());
    }

    @Override
    public String toString() {
        return String.format("Todo(id=%d, userId=%s, text=%s, status=%s, priority=%s, createdAt=%s, updatedAt=%s)",
                this.id, this.userId, this.toDoText, this.todoStatus, this.isPriority, this.createdAt, this.updatedAt);
    }

}
