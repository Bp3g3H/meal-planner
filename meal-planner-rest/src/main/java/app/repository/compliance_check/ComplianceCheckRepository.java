package app.repository.compliance_check;

import app.model.entity.compliance_check.ComplianceCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceCheckRepository extends JpaRepository<ComplianceCheck, UUID> {

    List<ComplianceCheck> findByExternalUserIdAndCheckDateBetween(UUID externalId, Date start, Date end);
}
