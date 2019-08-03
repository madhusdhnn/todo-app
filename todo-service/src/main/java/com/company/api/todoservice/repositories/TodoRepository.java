package com.company.api.todoservice.repositories;

import com.company.api.todoservice.domains.Todo;
import com.company.api.todoservice.domains.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<List<Todo>> findByUserIdAndTodoGroup(String userId, TodoGroup todoGroup);

    @Transactional
    @Modifying
    Integer deleteByUserIdAndId(String userId, Long id);

}
