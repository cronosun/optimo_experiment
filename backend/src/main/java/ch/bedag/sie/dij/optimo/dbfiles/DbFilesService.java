package ch.bedag.sie.dij.optimo.dbfiles;

import ch.bedag.sie.dij.optimo.files.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class DbFilesService {

    private final DbFileRepository repository;

    public DbFileId saveFile(File file) {
        var entity = new FileEntity();
        entity.setFilename(file.filename());
        entity.setContent(file.content());
        entity.setMimeType(file.mimeType());
        repository.save(entity);
        return new DbFileId(entity.getId());
    }

    public boolean fileExists(DbFileId fileId) {
        return repository.existsById(fileId.id());
    }

    public void delete(DbFileId fileId) {
        repository.deleteById(fileId.id());
    }

    public DbFile getFileById(DbFileId fileId) {
        var entity = repository.getById(fileId.id());
        return map(entity);
    }

    private DbFile map(FileEntity entity) {
        var id = new DbFileId(entity.getId());
        var mimeType = entity.getMimeType();
        var filename = entity.getFilename();
        var content = entity.getContent();
        return new DbFile(id, mimeType, filename, content);
    }
}
