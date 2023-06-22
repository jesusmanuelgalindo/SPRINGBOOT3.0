package gob.jmas.dto.facturamaResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Issuer {
    private String fiscalRegime;
    private String rfc;
    private String taxName;

    // Getter y Setter
}