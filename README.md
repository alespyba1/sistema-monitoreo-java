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
