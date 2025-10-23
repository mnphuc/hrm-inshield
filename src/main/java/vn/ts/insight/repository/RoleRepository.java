package vn.ts.insight.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ts.insight.domain.common.SystemRoleName;
import vn.ts.insight.domain.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(SystemRoleName name);
}
