# Sistema de Monitoreo de Tanques

## Integrantes

| Rol | Integrante | Usuario de GitHub |
| :--- | :--- | :--- |
| Estudiante A | Alejandro Spíndola Bazaldúa | alespyba1 |
| Estudiante B | Ricardo René Reséndiz Nieves | rresendiz42-scar |

## Descripción del sistema
Simular el funcionamiento de varios tanques de almacenamiento de líquido. Cada tanque guarda su capacidad, su nivel y
su estado de operación, y tiene un sensor que permite conocer cuánto líquido
contiene. El programa demuestra el llenado, el vaciado y el control de los
límites del proceso.

## Clases implementadas

| Clase | Responsabilidad |
| --- | --- |
| `Tanque` | Guarda los datos del tanque y cuida que el nivel nunca sea negativo ni pase de la capacidad máxima. Permite llenarlo, vaciarlo, detenerlo y consultar su porcentaje de llenado. |
| `SensorNivel` | Mide el nivel del tanque que tiene asignado, guarda esa lectura y dice si el valor está dentro de lo que ese tanque puede tener. |
| `TankState` | Enum con los tres estados de operación: LLENANDO, VACIANDO y DETENIDO. |
| `Main` | Crea los tanques con sus sensores, ejecuta la simulación y muestra los resultados en consola. |
| `PruebasSistema` | Ejecuta los nueve casos de prueba que pide la práctica e imprime el resultado esperado y el obtenido. |

## Cómo ejecutar el programa

1. Abrir el proyecto en IntelliJ IDEA.
2. Para ver la simulación, ejecutar `Main`.
3. Para ver las pruebas, ejecutar `PruebasSistema`.

## Pruebas realizadas

Tal como venia en la práctica se implemento una clase extra para probar diferentes casos, y así comprobar el correcto funcionamiento del sistema

```text
PRUEBAS DEL SISTEMA

CASO 1: Crear tanque con datos validos
Esperado: conserva id, capacidad, nivel y estado DETENIDO
Obtenido: T-01 | 1000.0 L | 650.0 L | DETENIDO

CASO 2: Llenar tanque
Nivel antes: 650.0 L
Esperado: el nivel aumenta
Obtenido: 800.0 L

CASO 3: Intentar superar la capacidad
Capacidad: 1000.0 L
Esperado: el nivel no pasa de la capacidad
Obtenido: 1000.0 L

CASO 4: Vaciar tanque
Nivel antes: 100.0 L
Esperado: el nivel disminuye
Obtenido: 60.0 L

CASO 5: Intentar obtener un nivel negativo
Nivel antes: 60.0 L
Esperado: el nivel nunca baja de cero
Obtenido: 0.0 L

CASO 6: Consultar porcentaje
Nivel: 1000.0 L de 2000.0 L
Esperado: 50 %
Obtenido: 50.0 %

CASO 7: Cambiar el estado de operacion
Estado inicial: DETENIDO
Despues de llenar: LLENANDO
Despues de vaciar: VACIANDO

CASO 8: Detener tanque
Estado antes: VACIANDO
Esperado: DETENIDO
Obtenido: DETENIDO

CASO 9: Consultar sensor
Nivel real del tanque: 1000.0 L
Esperado: la lectura coincide con el nivel
Obtenido: 1000.0 L
Lectura valida: true
```

## Responsabilidades de cada integrante

| Integrante | Aportación                                                                                                                                                   |
| --- |--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Alejandro Spíndola | Creación del repositorio, secciones 1 y 2 del análisis, diseño de clases, diagramas UML inicial, clase `Tanque`, integración del sistema y clase de pruebas. |
| Ricardo Reséndiz | Secciones 3 y 4 del análisis, clase `SensorNivel` y revisión del Pull Request de la clase `Tanque` y actualización del diagrama UML final .                  |


## Documento de análisis y diseño

[docs/01-analisis-diseno.md](docs/01-analisis-diseno.md)



## Conclusiones individuales

### Alejandro Spíndola Bazaldúa

Lo que más me sirvió entender fue para qué sirve el `private`. Al implementar el tanque quedó claro que es lo que hace cumplir la restricción del problema: si el nivel fuera público, cualquier línea del programa podría dejarlo negativo y no habría forma de evitarlo. Poniéndolo privado, la revisión de límites vive dentro de la clase y no hay manera de saltársela.

La parte de análisis y diseño antes de programar se me hizo tediosa al inicio, pero cuando llegué a escribir la clase ya sabía exactamente qué atributos y qué métodos necesitaba, y casi no tuve que improvisar. Los cambios que sí hubo fueron menores y quedaron documentados en el UML final.

---

# Evidencia individual

## Alejandro Spíndola Bazaldúa

**1. ¿Qué diferencia existe entre una clase y un objeto?**

La clase es el molde y el objeto es lo que sale de ese molde. `Tanque` es una sola clase, pero con ella creamos T-01, T-02 y T-03, que son tres objetos distintos, cada uno con sus propios datos.

**2. Mencione tres objetos creados durante la ejecución del programa.**

Los tanques T-01, T-02 y T-03. 

**3. ¿Por qué los atributos principales fueron declarados `private`?**

Colocandolo público, desde `Main` se le podría poner un número negativo o más grande que la capacidad y nada lo impediría. 

**4. ¿Qué responsabilidad tiene la clase que representa al tanque?**

Guardar sus datos y cuidar que el nivel se mantenga entre cero y su capacidad. 

**5. ¿Qué responsabilidad tiene la clase que representa al sensor?**

Medir. Le pregunta el nivel al tanque que tiene asignado, guarda ese valor y dice si la lectura es coherente.

**6. ¿Qué cambio realizaron al UML después de implementar el programa?**

Fue más que nada el nombre de la clase `Sensor` quedó como `SensorNivel` y le cambiamos los nombres a sus atributos y métodos, se quitó `mostrarLectura()`, el estado se resolvió con el enum `TankState` en vez de texto, y se agregó la clase `PruebasSistema` que no estaba inicialmente, los nombres fueron cambiados para tener un nombre general en ambos codigos, y evitar confusiones en lo que cada quien trabajaba por su parte.

**7. ¿Qué observación técnica realizó durante el Pull Request de su compañero?**

Que `realizarLectura()` devolvía `valorMedido` sin haberlo actualizado nunca, entonces el sensor siempre iba a marcar cero. Faltaba que el método consultara el nivel del tanque con `getNivelActual()`. 

**8. ¿Qué corrección realizó a partir de una observación recibida?**

En `obtenerPorcentajeLlenado()` se dividía entre la capacidad sin revisar que no fuera cero. Le agregué una validación para que devuelva cero en ese caso en vez de dar un resultado inválido.

**9. ¿Qué aportó personalmente al proyecto?**

Puse en práctica lo visto en clase como son los constructores, puse en práctica algunos temas también, considero que mejoro considerablemente la forma de trabajar en github, se ,e hizo mucho mas sencillo que la práctica pasada, también aprendí a usar los puml, y como hacer diagramas e implementarlos, por ultimo siguiendo este orden de organización se puede obtener un buen resultado, ya que orimero te centras en identificar y describir como seria el funcionamiento del sistema, asi como sul clases que usaras, su funcionamiento de esas clases dentro del sistema, asi como el nombre de las variables a utilizar.

**10. ¿Qué decisión de diseño le pareció más importante y por qué?**

Personalmente considero que la parte de las primeras tablas donde te pide justamente las clases implementadas que se utilizaran, así como la creación del puml, ambas me ayudaron a saber que tenía que hacer todo el tiempo, me ayudaron a guiarm de una manera mas veloz.

---

## Ricardo René Reséndiz Nieves
