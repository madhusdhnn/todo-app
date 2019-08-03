package com.company.api.todoservice.repositories;

import com.company.api.todoservice.domains.TodoGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoGroupRepository extends JpaRepository<TodoGroup, Long> {

    Optional<TodoGroup> findByGroupNameAndUserId(String groupName, String userId);

    Optional<TodoGroup> findByIdAndUserIdEquals(Long id, String userId);
}
