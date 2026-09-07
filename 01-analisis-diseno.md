| Rol | Integrante                   | Usuario de GitHub | Fecha de inicio |
    | :--- |:-----------------------------|:------------------| :--- |
    | Estudiante A | Alejandro Spindola           | alespyba1         | Jueves 9 de septiembre de 2026 |
    | Estudiante B | Ricardo René Reséndiz Nieves | rresendiz42-scar  | Jueves 9 de septiembre de 2026 |

## 1. Descripción del problema

### Qué sistema se pretende representar

Se busca representar, en forma de programa, una instalación industrial que cuenta con varios tanques de almacenamiento de líquido. Cada tanque funciona de manera independiente: tiene su propia capacidad, su propio contenido y su propia condición de operación en un momento dado. Además, cada tanque cuenta con un sensor que permite conocer cuánto líquido contiene sin necesidad de inspeccionarlo directamente.

El programa no controla equipo físico real. Es una simulación de consola cuyo propósito es reproducir el comportamiento lógico del proceso: que un tanque se llene, se vacíe, se detenga y pueda ser consultado, respetando en todo momento los límites físicos que tendría un tanque real.

### Qué información necesita manejar

Para que la simulación tenga sentido, el sistema debe conservar:

- **Identificación de cada tanque**, ya que la instalación tiene varios y deben poder distinguirse entre sí (T-01, T-02, T-03).
- **Capacidad máxima**, expresada en litros. Es un dato fijo que corresponde a la construcción física del tanque y no debería cambiar durante la operación.
- **Nivel actual**, también en litros. Es el dato que varía continuamente conforme el tanque se llena o se vacía.
- **Estado de operación**, que indica qué está haciendo el tanque en este momento: `DETENIDO`, `LLENANDO` o `VACIANDO`.
- **Identificación del sensor** asociado y el valor de su última lectura, para poder rastrear de dónde proviene la medición.

### Qué operaciones debe realizar

El sistema debe permitir dos tipos de acciones. Por un lado, **acciones que modifican** la condición del tanque: agregar líquido, retirar líquido y detener la operación. Por otro lado, **consultas que no modifican nada**: conocer el nivel actual, conocer el porcentaje de ocupación respecto a la capacidad, conocer el estado de operación y mostrar el resumen completo de la información del tanque.

Adicionalmente, el sensor debe poder realizar una lectura del tanque que vigila, entregar el valor medido y señalar si esa lectura resulta coherente, es decir, si cae dentro del intervalo que físicamente tiene sentido para ese tanque.

### Qué restricciones deben respetarse

La restricción central es que el contenido del tanque siempre debe cumplir:

```text
0 <= nivelActual <= capacidadMaxima
```

Esto tiene una justificación física evidente: un tanque no puede contener litros negativos, ni puede almacenar más líquido del que cabe en él. Por lo tanto, si se solicita un llenado que excedería la capacidad, el tanque debe llenarse únicamente hasta su tope; y si se solicita un vaciado mayor al contenido disponible, el tanque debe quedar en cero, no en un valor negativo.

De esta restricción se desprende una consecuencia importante para el diseño: el nivel no puede ser un dato que cualquier parte del programa modifique libremente. Si desde `Main` fuera posible asignar directamente un valor al nivel, la restricción podría violarse sin que nada lo impidiera. Por eso el propio tanque debe ser el responsable de vigilar sus límites, y toda modificación debe pasar por sus métodos.

Además, el estado de operación debe corresponder a lo que realmente ocurrió: no tiene sentido que un tanque quede marcado como `LLENANDO` después de que se le solicitó detenerse.

---

