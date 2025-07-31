package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.SupportConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportConfigRepository extends JpaRepository<SupportConfig, String> {
    
    Optional<SupportConfig> findByIsActiveTrue();
    
    Optional<SupportConfig> findFirstByOrderByCreatedAtDesc();
} 