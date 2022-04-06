package ch.bedag.sie.dij.optimo.dbfiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// INFO: Repositories können nun package private sein
@Repository
interface DbFileRepository extends JpaRepository<FileEntity, UUID> {
}
