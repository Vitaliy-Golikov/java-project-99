package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByName(String name);

    @Override
    @EntityGraph(attributePaths = {"taskStatus", "assignee", "labels"})
    List<Task> findAll();

    @Override
    @EntityGraph(attributePaths = {"taskStatus", "assignee", "labels"})
    List<Task> findAll(org.springframework.data.jpa.domain.Specification<Task> spec);

    @Override
    @EntityGraph(attributePaths = {"taskStatus", "assignee", "labels"})
    Optional<Task> findById(Long id);
}