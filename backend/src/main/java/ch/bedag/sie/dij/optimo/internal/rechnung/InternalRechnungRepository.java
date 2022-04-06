package ch.bedag.sie.dij.optimo.internal.rechnung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// INFO: Reposiories können nun package private sein
@Repository
interface InternalRechnungRepository extends JpaRepository<InternalRechnungEntity, UUID> {
}
