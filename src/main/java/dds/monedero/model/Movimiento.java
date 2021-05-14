package dds.monedero.model;

import dds.monedero.exceptions.MontoNegativoException;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  // Nota: En ningún lenguaje de programación usen jamás doubles (es decir, números con punto flotante) para modelar dinero en el mundo real.
  // En su lugar siempre usen numeros de precision arbitraria o punto fijo, como BigDecimal en Java y similares
  // De todas formas, NO es necesario modificar ésto como parte de este ejercicio.
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    validarSiEsMontoNegativo(monto);
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return this.monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void validarSiEsMontoNegativo(double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }
}
