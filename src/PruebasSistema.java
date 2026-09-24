public class PruebasSistema {
    public static void main(String[] args) {

        System.out.println("PRUEBAS DEL SISTEMA");
        System.out.println();

        // CASO 1: Crear tanque con datos validos
        System.out.println("CASO 1: Crear tanque con datos validos");
        Tanque t1 = new Tanque("T-01", 1000, 650);
        System.out.println("Esperado: conserva id, capacidad, nivel y estado DETENIDO");
        System.out.println("Obtenido: " + t1.getId() + " | " + t1.getCapacidadMaxima()
                + " L | " + t1.getNivelActual() + " L | " + t1.getEstado());
        System.out.println();

        // CASO 2: Llenar tanque
        System.out.println("CASO 2: Llenar tanque");
        System.out.println("Nivel antes: " + t1.getNivelActual() + " L");
        t1.llenar(150);
        System.out.println("Esperado: el nivel aumenta");
        System.out.println("Obtenido: " + t1.getNivelActual() + " L");
        System.out.println();

        // CASO 3: Intentar superar la capacidad
        System.out.println("CASO 3: Intentar superar la capacidad");
        System.out.println("Capacidad: " + t1.getCapacidadMaxima() + " L");
        t1.llenar(5000);
        System.out.println("Esperado: el nivel no pasa de la capacidad");
        System.out.println("Obtenido: " + t1.getNivelActual() + " L");
        System.out.println();

        // CASO 4: Vaciar tanque
        System.out.println("CASO 4: Vaciar tanque");
        Tanque t2 = new Tanque("T-02", 500, 100);
        System.out.println("Nivel antes: " + t2.getNivelActual() + " L");
        t2.vaciar(40);
        System.out.println("Esperado: el nivel disminuye");
        System.out.println("Obtenido: " + t2.getNivelActual() + " L");
        System.out.println();

        // CASO 5: Intentar obtener un nivel negativo
        System.out.println("CASO 5: Intentar obtener un nivel negativo");
        System.out.println("Nivel antes: " + t2.getNivelActual() + " L");
        t2.vaciar(9000);
        System.out.println("Esperado: el nivel nunca baja de cero");
        System.out.println("Obtenido: " + t2.getNivelActual() + " L");
        System.out.println();

        // CASO 6: Consultar porcentaje
        System.out.println("CASO 6: Consultar porcentaje");
        Tanque t3 = new Tanque("T-03", 2000, 1000);
        System.out.println("Nivel: " + t3.getNivelActual() + " L de "
                + t3.getCapacidadMaxima() + " L");
        System.out.println("Esperado: 50 %");
        System.out.println("Obtenido: " + t3.obtenerPorcentajeLlenado() + " %");
        System.out.println();

        // CASO 7: Cambiar el estado de operacion
        System.out.println("CASO 7: Cambiar el estado de operacion");
        System.out.println("Estado inicial: " + t3.getEstado());
        t3.llenar(100);
        System.out.println("Despues de llenar: " + t3.getEstado());
        t3.vaciar(100);
        System.out.println("Despues de vaciar: " + t3.getEstado());
        System.out.println();

        // CASO 8: Detener tanque
        System.out.println("CASO 8: Detener tanque");
        System.out.println("Estado antes: " + t3.getEstado());
        t3.detener();
        System.out.println("Esperado: DETENIDO");
        System.out.println("Obtenido: " + t3.getEstado());
        System.out.println();

        // CASO 9: Consultar sensor
        System.out.println("CASO 9: Consultar sensor");
        SensorNivel s1 = new SensorNivel("SN-01", t1);
        System.out.println("Nivel real del tanque: " + t1.getNivelActual() + " L");
        System.out.println("Esperado: la lectura coincide con el nivel");
        System.out.println("Obtenido: " + s1.realizarLectura() + " L");
        System.out.println("Lectura valida: " + s1.lecturaValida());
        System.out.println();

    }
}