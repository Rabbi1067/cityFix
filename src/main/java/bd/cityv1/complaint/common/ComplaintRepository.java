package bd.cityv1.complaint.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findAllByCitizen_IdOrderByCreatedAtDesc(Long citizenId);

    long countByCitizen_IdAndStatusNot(Long citizenId, Status status);
    long countByCitizen_IdAndStatus(Long citizenId, Status status);

    long countByCitizenIdAndStatusNot(Long citizenId, Status status);
    long countByCitizenIdAndStatus(Long citizenId, Status status);

    List<Complaint> findAllByOrderByCreatedAtDesc();

    @Query("""
            select distinct c
            from Complaint c
            left join fetch c.resolution
            left join fetch c.citizen
            where c.id = :id
            """)
    Optional<Complaint> findDetailsById(@Param("id") Long id);

    @Query("""
            select distinct c
            from Complaint c
            join fetch c.resolution r
            where r.resolvedAt is not null
            order by r.resolvedAt desc
            """)
    List<Complaint> findAllWithResolutionDetails();
}