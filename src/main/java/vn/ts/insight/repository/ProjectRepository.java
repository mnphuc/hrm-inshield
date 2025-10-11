package vn.ts.insight.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.project.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByCode(String code);
}
