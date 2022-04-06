package ch.bedag.sie.dij.optimo.dbfiles;

import ch.bedag.sie.dij.optimo.files.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class DbFilesTestService {
    private final DbFilesService dbFilesService;

    /**
     * Just saves a file in the database.*
     */
    public DbFileId saveAnyFile() {
        return dbFilesService.saveFile(new File() {
            @Override
            public String filename() {
                return "JustSomeFilename.pdf";
            }

            @Override
            public String mimeType() {
                return "application/pdf";
            }

            @Override
            public byte[] content() {
                return "THIS IS THE PDF_CONTENT".getBytes(StandardCharsets.UTF_8);
            }
        });
    }

}
