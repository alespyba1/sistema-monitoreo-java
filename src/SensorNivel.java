public class SensorNivel {

    private String identificador;
    private Tanque tanque;
    private double valorMedido;

    public SensorNivel(String identificador, Tanque tanque) {
        this.identificador = identificador;
        this.tanque = tanque;
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