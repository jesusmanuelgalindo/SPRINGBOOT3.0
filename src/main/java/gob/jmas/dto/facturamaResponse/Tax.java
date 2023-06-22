package gob.jmas.dto.facturamaResponse;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tax {
    private double total;
    private String name;
    private double rate;
    private String type;

    // Getter y Setter
}
