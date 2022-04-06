package ch.bedag.sie.dij.optimo.dabbawala.simulator;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocument;
import ch.bedag.sie.dij.optimo.dabbawala.DabDocumentId;
import ch.bedag.sie.dij.optimo.dabbawala.DabMandant;
import ch.bedag.sie.dij.optimo.dabbawala.DabRole;
import ch.bedag.sie.dij.optimo.dabbawala.Dabbawala;
import ch.bedag.sie.dij.optimo.files.File;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DabbawalaSimulatorService implements Dabbawala {

    private final HashSet<DabMandant> mandanten;
    private final HashSet<DabRole> roles;
    private final InMemoryDocumentStore documentStore;

    public DabbawalaSimulatorService() {
        this.mandanten = new HashSet<>();
        this.roles = new HashSet<>();
        this.documentStore = new InMemoryDocumentStore();
    }

    @Override
    public Set<DabMandant> getMandanten() {
        return mandanten;
    }

    @Override
    public Set<DabRole> getRoles() {
        return roles;
    }

    @Override
    public DabDocumentId saveDocument(File file) {
        return documentStore.saveDocument(file);
    }

    @Override
    public DabDocument getDocumentById(DabDocumentId id) {
        return documentStore.getDocumentById(id);
    }

    public void addRole(DabRole role) {
        this.roles.add(role);
    }

    public void removeAllRoles() {
        this.roles.clear();
    }

    public void removeRole(DabRole role) {
        this.roles.remove(role);
    }
}
