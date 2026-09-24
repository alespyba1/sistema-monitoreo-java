public class SensorNivel {

    private String identificador;
    private Tanque tanque;
    private double valorMedido;

    public SensorNivel(String identificador, Tanque tanque) {
        this.identificador = identificador;
        this.tanque = tanque;
        this.valorMedido = tanque.getNivelActual();
    }

    public double realizarLectura() {
        valorMedido = tanque.getNivelActual();
        return valorMedido;
    }

    public double getValorMedido() {
        return valorMedido;
    }

    public boolean lecturaValida() {
        return valorMedido >= 0 && valorMedido <= tanque.getCapacidadMaxima();
    }

    public String getIdentificador() {
        return identificador;
    }
}