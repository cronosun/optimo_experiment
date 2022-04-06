package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.model.RechnungId;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/rechnung")
@Log4j2
@RequiredArgsConstructor
// TODO: Auth
public class ExternalRechnungApi {

    private final ExternalRechnungService service;

    @PostMapping("/userEnteredRechnung")
    public ResponseEntity<RechnungId> userEnteredRechnung(EnteredRechnung rechnung) {
        var id = service.saveUserEnteredRechnung(rechnung);
        return ResponseEntity.ok(id);
    }
}
