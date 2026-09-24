public class Main {
    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE MONITOREO ===");
        System.out.println();

        // 1. Creación de objetos mediante constructores
        Tanque t1 = new Tanque("T-01", 1000, 650);
        Tanque t2 = new Tanque("T-02", 500, 100);
        Tanque t3 = new Tanque("T-03", 2000, 1800);

        SensorNivel s1 = new SensorNivel("SN-01", t1);
        SensorNivel s2 = new SensorNivel("SN-02", t2);
        SensorNivel s3 = new SensorNivel("SN-03", t3);

        // 2. Consulta del estado inicial
        System.out.println("--- ESTADO INICIAL ---");
        System.out.println();
        t1.mostrarInformacion();
        t2.mostrarInformacion();
        t3.mostrarInformacion();

        // 3. Llenado
        System.out.println("--- LLENANDO T-01 CON 150 L ---");
        System.out.println();
        t1.llenar(150);
        t1.mostrarInformacion();

        // 4. Vaciado
        System.out.println("--- VACIANDO T-02 CON 40 L ---");
        System.out.println();
        t2.vaciar(40);
        t2.mostrarInformacion();

        // 5. Intento de superar la capacidad máxima
        System.out.println("--- INTENTANDO LLENAR T-03 CON 5000 L ---");
        System.out.println("Capacidad de T-03: " + t3.getCapacidadMaxima() + " L");
        System.out.println();
        t3.llenar(5000);
        t3.mostrarInformacion();
        System.out.println("El nivel se quedo en el maximo, no se paso.");
        System.out.println();

        // 6. Intento de disminuir el nivel por debajo de cero
        System.out.println("--- INTENTANDO VACIAR T-02 CON 9000 L ---");
        System.out.println("Nivel de T-02 antes: " + t2.getNivelActual() + " L");
        System.out.println();
        t2.vaciar(9000);
        t2.mostrarInformacion();
        System.out.println("El nivel se quedo en cero, no fue negativo.");
        System.out.println();

        // 7. Lectura mediante sensor
        System.out.println("--- LECTURAS DE LOS SENSORES ---");
        System.out.println();
        System.out.println("Sensor " + s1.getIdentificador() + " -> " + s1.realizarLectura() + " L");
        System.out.println("Sensor " + s2.getIdentificador() + " -> " + s2.realizarLectura() + " L");
        System.out.println("Sensor " + s3.getIdentificador() + " -> " + s3.realizarLectura() + " L");
        System.out.println();
        System.out.println("Lectura de " + s1.getIdentificador() + " valida: " + s1.lecturaValida());
        System.out.println();

        // 8. Cálculo del porcentaje de llenado
        System.out.println("--- PORCENTAJES DE LLENADO ---");
        System.out.println();
        System.out.println(t1.getId() + ": " + t1.obtenerPorcentajeLlenado() + " %");
        System.out.println(t2.getId() + ": " + t2.obtenerPorcentajeLlenado() + " %");
        System.out.println(t3.getId() + ": " + t3.obtenerPorcentajeLlenado() + " %");
        System.out.println();

        // 9. Cambio de estado de operación
        System.out.println("--- DETENIENDO TODOS LOS TANQUES ---");
        System.out.println();
        System.out.println("Estado de T-01 antes: " + t1.getEstado());
        t1.detener();
        System.out.println("Estado de T-01 despues: " + t1.getEstado());
        System.out.println();

        t2.detener();
        t3.detener();

        System.out.println("--- ESTADO FINAL ---");
        System.out.println();
        t1.mostrarInformacion();
        t2.mostrarInformacion();
        t3.mostrarInformacion();
    }
}