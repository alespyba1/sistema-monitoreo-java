| Rol | Integrante | Usuario de GitHub | Fecha de inicio |
| :--- | :--- | :--- | :--- |
| Estudiante A | Alejandro Spindola | alespyba1 | Miércoles 9 de septiembre de 2026 |
| Estudiante B | Ricardo René Reséndiz Nieves | rresendiz42-scar | Miércoles 9 de septiembre de 2026 |

---

## 1. Descripción del problema

### Qué sistema se pretende representar
El programa busca representar un sistema de tanques de almacenamiento de liquidos. Cada tanque funciona por su cuenta tiene su propia capacidad, su propio contenido y su propia condición de operación, cada tanque tiene sensor que permite conocer el nivel de líquido que contiene.



### Qué información necesita manejar
Número de tanque, Capacidad, Nivel actual, Estado de operación. Identificación del sensor. 

### Qué operaciones debe realizar

consultar la información de un tanque

llenar un tanque

vaciar un tanque

detener su operación

consultar su nivel

consultar su porcntaje de llenado

consultar su estado

obtener una lectura mediante un sensor de nivel

Estas son las operaciones, necesarias que se requieren para el correcto funcionamiento del sistema,
logrando que se lleva de forma correcta la práctica,

### Qué restricciones deben respetarse

La principal restricción es que no puede tener nivel negativo, esto por 
obvias razones, asi como tampoco podrá superar la capacidad del tanque.


## 2. Identificación de objetos

De la descripción del problema se desprenden dos elementos con identidad propia, y un tercer componente que cumple una función distinta dentro del programa.

### Tanque

**Qué representa.** Un tanque físico de almacenamiento, con su capacidad, su contenido y su condición de operación.

**Por qué debe existir como objeto.** Por que al ser 3 tanques diferentes, estos tienen sus propias datos, y
estos son ajenos a entre tanques

**Qué responsabilidad tendría.** Sería el que se encargue practicamente de la medición y almacenamiento
de los datos en cada tanque(objeto), así nos aseguramos un mejor monitoreo.

### Sensor

**Qué representa.** El sensor que va instalado en el tanque y sirve para saber cuánto líquido tiene.

**Por qué debe existir como objeto.** Es el que se encarga de detectar si el tanque
ya llego a su limite, o no.

**Qué responsabilidad tendría.** Leer el nivel del tanque que le toca, guardar esa lectura y decir si el valor es válido o no.

Lo que el sensor no hace es guardar el nivel real ni cambiarlo. Ese dato es del tanque.

---

## 3. Estado y comportamiento

| Objeto propuesto | Responsabilidad                                                                 | Información que debe conservar | Comportamientos que debe realizar                                                                                                                |
| --- |---------------------------------------------------------------------------------| --- |--------------------------------------------------------------------------------------------------------------------------------------------------|
| Tanque | Representar un tanque de almacenamiento y medir su nivel y estado de operación. | Identificador, capacidad máxima, nivel actual y estado de operación. | Poder consultar su información, llenar el tanque, vaciarlo, detener su operación, consultar el nivel, obtener el porcentaje de llenado y conocer su estado. |
| Sensor de nivel | Obtener la lectura del nivel de líquido en un tanque.                           | Tanque al que está asociado y la lectura obtenida. | Obtener una lectura del nivel actual del tanque y proporcionar dicha lectura.                                                                    |

---

## 4. Relaciones entre los objetos

El sensor de nivel necesita relacionarse con un tanque, debido a que su función depende de conocer el nivel actual del tanque que está monitoreando.

La relación será entre el Sensor y el Tanque, el sensor estará asociado a un tanque y podrá obtener información sobre su nivel actual para realizar la lectura correspondiente.

El tanque será responsable de conservar y modificar su propio nivel, mientras que el sensor será responsable únicamente de obtener la lectura.

El tanque no debe encargarse del funcionamiento del sensor y el sensor no debe modificar directamente el nivel del tanque. Cada objeto debe encargarse de se función unicamente

---
