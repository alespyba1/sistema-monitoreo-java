enum TankState{
    LLENADO, VACIADO, DETENIDO;
}

public class Tanque {
    private String Id;
    private double capacidadMaxima;
    private double nivelActual;
    private TankState estado;


    public Tanque(String Id, double capacidadMaxima, double nivelInicial) {
        this.Id = Id;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = TankState.DETENIDO;

        if (nivelInicial < 0) {
            this.nivelActual = 0;
        }

        else if (nivelInicial > capacidadMaxima) {
            this.nivelActual = capacidadMaxima;
        }

        else {
            this.nivelActual = nivelInicial;
        }
    }
}




