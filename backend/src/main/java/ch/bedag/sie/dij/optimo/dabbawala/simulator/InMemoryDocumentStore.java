package ch.bedag.sie.dij.optimo.dabbawala.simulator;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocument;
import ch.bedag.sie.dij.optimo.dabbawala.DabDocumentId;
import ch.bedag.sie.dij.optimo.files.File;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class InMemoryDocumentStore {

    private final Map<UUID, InMemoryDabDocument> documents = new HashMap<>();

    public DabDocumentId saveDocument(File file) {
        var newUuid = UUID.randomUUID();
        documents.put(newUuid, InMemoryDabDocument.fromFile(file));
        return new DabDocumentId(newUuid);
    }

    public DabDocument getDocumentById(DabDocumentId id) {
        return documents.get(id.value());
    }
}
