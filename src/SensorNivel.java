public class SensorNivel {

    private String identificador;
    private double valorMedido;

    public SensorNivel(String identificador) {
        this.identificador = identificador;
        this.valorMedido = 0;
    }

    public double realizarLectura() {
        return valorMedido;
    }

    public double getValorMedido() {
        return valorMedido;
    }

    public boolean lecturaValida() {
        return valorMedido >= 0;
    }

    public String getIdentificador() {
        return identificador;
    }
}