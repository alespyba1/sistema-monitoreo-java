| Rol | Integrante                   | Usuario de GitHub | Fecha de inicio |
    | :--- |:-----------------------------|:------------------| :--- |
    | Estudiante A | Alejandro Spindola           | alespyba1         | Jueves 9 de septiembre de 2026 |
    | Estudiante B | Ricardo René Reséndiz Nieves | rresendiz42-scar  | Jueves 9 de septiembre de 2026 |

---

## 2. Identificación de objetos

De la descripción del problema se desprenden dos elementos con identidad propia, y un tercer componente que cumple una función distinta dentro del programa.

### 2.1 Tanque

**Qué representa.** Un tanque físico de almacenamiento de la instalación, con su capacidad, su contenido y su condición de operación.

**Por qué debe existir como objeto.** Porque la instalación tiene varios tanques y cada uno mantiene sus propios datos de forma simultánea e independiente: T-01 puede estar llenándose con 800 L mientras T-02 está detenido con 120 L. Si se intentara resolver el problema con variables sueltas, se necesitarían tres variables por cada tanque y no habría manera de mantenerlas asociadas entre sí. Una clase permite que cada tanque exista como una unidad completa, con su información y su comportamiento juntos, y que crear un tanque más sea simplemente crear otro objeto.

**Qué responsabilidad tendría.** El tanque es el responsable de conservar y proteger su propia información. Debe permitir que se le agregue o retire líquido, pero garantizando siempre que su contenido permanezca entre cero y su capacidad máxima. También debe informar su nivel, su porcentaje de ocupación y su estado cuando se le consulte, y actualizar ese estado de acuerdo con la operación que se le solicitó.

### 2.2 SensorNivel

**Qué representa.** El dispositivo de medición instalado en un tanque, que permite conocer su nivel sin acceder directamente al equipo.

**Por qué debe existir como objeto.** Aunque podría parecer suficiente con preguntarle el nivel al tanque, en un sistema automatizado real el sensor es un elemento distinto e identificable: tiene su propia etiqueta (SN-01), puede fallar, puede entregar lecturas fuera de rango y es el punto por donde la información del proceso llega al sistema de monitoreo. Representarlo como objeto refleja esa separación entre *el proceso* y *el medio por el cual se observa el proceso*, que es una distinción fundamental en automatización.

**Qué responsabilidad tendría.** El sensor es responsable de obtener una lectura del tanque que vigila, conservar el valor de esa última medición y poder informarlo. Además debe indicar si la lectura obtenida es válida, es decir, si se encuentra dentro del intervalo aceptable para ese tanque.

Es importante señalar lo que el sensor **no** debe hacer: no almacena el nivel real del tanque ni lo modifica. El nivel verdadero pertenece al tanque; el sensor únicamente guarda una copia del último valor que observó.

### 2.3 El estado de operación: por qué no lo tratamos como objeto

Consideramos si `DETENIDO`, `LLENANDO` y `VACIANDO` debían formar un objeto aparte. Concluimos que no, porque el estado no tiene información propia ni comportamiento propio: es únicamente un dato que describe al tanque en un instante determinado, del mismo modo que el nivel. Por lo tanto lo trataremos como un atributo del tanque, con valores restringidos a esas tres opciones.

### 2.4 El programa de monitoreo

Finalmente, se requiere un componente que cree los tanques y sus sensores, ejecute la secuencia de operaciones y muestre los resultados en consola. No representa un elemento físico del proceso, sino el punto de arranque del programa y el encargado de la presentación de la información. Por esa razón no lo consideramos un objeto del dominio del problema, sino la clase principal (`Main`) desde la cual se utilizan los objetos anteriores.

### 2.5 Resumen de objetos identificados

Quedan entonces dos objetos del dominio, `Tanque` y `SensorNivel`, cuyas responsabilidades concretas, información a conservar y comportamientos se detallan en la sección 3, y cuya colaboración se analiza en la sección 4.

---

## 3. Estado y comportamiento

---

## 4. Relaciones entre los objetos