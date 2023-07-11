package gob.jmas.dto.facturamaException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FacturamaException {
    private ModelException Model;

    public FacturamaException(ModelException model) {
        Model = model;
    }
}
