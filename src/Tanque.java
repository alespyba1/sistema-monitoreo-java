enum TankState {
    LLENANDO, VACIANDO, DETENIDO;
}

public class Tanque {

    private String id;
    private double capacidadMaxima;
    private double nivelActual;
    private TankState estado;

    public Tanque(String id, double capacidadMaxima, double nivelInicial) {
        this.id = id;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = TankState.DETENIDO;

        if (nivelInicial < 0) {
            this.nivelActual = 0;
        } else if (nivelInicial > capacidadMaxima) {
            this.nivelActual = capacidadMaxima;
        } else {
            this.nivelActual = nivelInicial;
        }
    }

    public String getId() {
        return id;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getNivelActual() {
        return nivelActual;
    }

    public TankState getEstado() {
        return estado;
    }

    public void llenar(double litros) {
        if (litros <= 0) {
            return;
        }

        estado = TankState.LLENANDO;
        nivelActual = nivelActual + litros;

        if (nivelActual > capacidadMaxima) {
            nivelActual = capacidadMaxima;
        }
    }

    public void vaciar(double litros) {
        if (litros <= 0) {
            return;
        }

        estado = TankState.VACIANDO;
        nivelActual = nivelActual - litros;

        if (nivelActual < 0) {
            nivelActual = 0;
        }
    }
}

