package gob.jmas.dto.facturamaResponse;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaxStamp {
    private String uuid;
    private LocalDateTime date;
    private String cfdiSign;
    private String satCertNumber;
    private String satSign;
    private String rfcProvCertif;

    // Getter y Setter
}