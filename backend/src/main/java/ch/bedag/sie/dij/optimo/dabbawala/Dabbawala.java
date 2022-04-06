package ch.bedag.sie.dij.optimo.dabbawala;

import ch.bedag.sie.dij.optimo.files.File;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Functionality provided by dabbawala, such as user management and document store.
 */
public interface Dabbawala {
    Set<DabMandant> getMandanten();

    Set<DabRole> getRoles();

    DabDocumentId saveDocument(File file);

    @Nullable
    DabDocument getDocumentById(DabDocumentId id);
}
