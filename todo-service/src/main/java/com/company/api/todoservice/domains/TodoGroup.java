package com.company.api.todoservice.domains;

import com.company.api.todoservice.utils.CollectionUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "todo_group")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TodoGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "todoGroup", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Todo> todos = new ArrayList<>();

    public TodoGroup(String userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
    }

    public TodoGroup(String userId, String groupName, List<Todo> todos) {
        this(userId, groupName);
        this.todos = todos;
    }

    public void setGroupName(String newName) {
        this.groupName = newName;
    }

    @Override
    public String toString() {
        boolean todosFound = !CollectionUtils.isEmpty(this.todos);
        return String.format("TodoGroup(id=%d, userId=%s, groupName=%s, createdAt=%s, updatedAt=%s%s)",
                this.id, this.userId, this.groupName,
                this.createdAt, this.updatedAt, todosFound ? String.format(", todos=%s", this.todos.toString()) : "");
    }

}
