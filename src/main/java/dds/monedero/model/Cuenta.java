package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();
  private int cantidadMaximaDeMovimientos = 3;
  private double montoMaximoDeExtraccion = 1000;

  public Cuenta() { }

  public void poner(double cuanto) {
    validarSiEsMontoNegativo(cuanto);
    validarCantidadDeDepositosDiarios();
    agregarMovimiento(LocalDate.now(), cuanto, true);
  }

  public void sacar(double cuanto) {
    validarSiEsMontoNegativo(cuanto);
    validarMontoDeExtraccion(cuanto);
    validarLimiteDeExtraccion(cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
  }

  public void validarSiEsMontoNegativo(double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarMontoDeExtraccion(double cuanto){
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void validarCantidadDeDepositosDiarios(){
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= cantidadMaximaDeMovimientos) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantidadMaximaDeMovimientos + " depositos diarios");
    }
  }

  public void validarLimiteDeExtraccion(double cuanto){
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = montoMaximoDeExtraccion - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + montoMaximoDeExtraccion
          + " diarios, límite: " + limite);
    }
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
    this.actualizarSaldo(movimiento.getMonto());
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void actualizarSaldo(double adicional) {
    this.saldo += adicional;
  }

}
