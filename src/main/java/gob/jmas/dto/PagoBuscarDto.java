package gob.jmas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class PagoBuscarDto {

    @NotNull(message = "Debe especificar numero de cuenta")
    @Pattern(regexp = "\\d+", message = "La cuenta debe ser un numero")
    private String cuenta;

    @NotNull(message = "Debe especificar numero de caja")
    @Digits(integer = 8, fraction = 0, message = "La caja debe ser un número entero de hasta 8 dígitos")

    private Integer caja;

    @NotNull(message = "Debe especificar la referencia que aparece en el ticket")
    @Digits(integer = 8, fraction = 0, message = "La referencia debe ser un número entero de hasta 8 dígitos")
    private Integer referencia;
//    @Min(value = 1, message = "El valor mínimo permitido es 1")
//    @Max(value = 100, message = "El valor máximo permitido es 100")
}
