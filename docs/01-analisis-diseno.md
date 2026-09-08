| Rol | Integrante | Usuario de GitHub | Fecha de inicio |
| :--- | :--- | :--- | :--- |
| Estudiante A | Alejandro Spindola | alespyba1 | Miércoles 9 de septiembre de 2026 |
| Estudiante B | Ricardo René Reséndiz Nieves | rresendiz42-scar | Miércoles 9 de septiembre de 2026 |

---

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

Sobre estos datos asumimos lo siguiente: la capacidad y el nivel se manejan en litros como valores numéricos que admiten decimales, ya que una lectura de sensor rara vez arroja un valor entero exacto. El identificador del tanque y el del sensor son cadenas de texto, porque siguen un formato como T-01 o SN-01 que combina letras y números. El estado de operación, aunque se escribe como texto, solo puede tomar uno de los tres valores previstos.

### Qué operaciones debe realizar

El sistema debe permitir dos tipos de acciones. Por un lado, **acciones que modifican** la condición del tanque: agregar líquido, retirar líquido y detener la operación. Por otro lado, **consultas que no modifican nada**: conocer el nivel actual, conocer el porcentaje de ocupación respecto a la capacidad, conocer el estado de operación y mostrar el resumen completo de la información del tanque.

Adicionalmente, el sensor debe poder realizar una lectura del tanque que vigila, entregar el valor medido y señalar si esa lectura resulta coherente, es decir, si cae dentro del intervalo que físicamente tiene sentido para ese tanque.

Conviene señalar que estas operaciones se aplican a cada tanque de manera individual. El sistema trabaja con varios tanques al mismo tiempo, y una orden de llenado dirigida a T-01 no debe alterar en absoluto la condición de T-02 ni de T-03.

### Qué restricciones deben respetarse

La restricción central es que el contenido del tanque siempre debe cumplir:

```text
0 <= nivelActual <= capacidadMaxima
```

Esto tiene una justificación física evidente: un tanque no puede contener litros negativos, ni puede almacenar más líquido del que cabe en él. Decidimos que, ante una operación que rompería el límite, el tanque no rechace la orden por completo sino que la ejecute hasta donde le sea posible. Es decir, si a un tanque de 1000 L con 900 L se le solicitan 300 L más, se llenará hasta 1000 L y descartará el excedente; y si a un tanque con 200 L se le pide vaciar 500 L, quedará en 0 L en lugar de un valor negativo. Nos pareció el comportamiento más cercano al de un tanque real, donde el líquido sobrante simplemente se derrama o la bomba se queda sin succión.

De esta restricción se desprende una consecuencia importante para el diseño: el nivel no puede ser un dato que cualquier parte del programa modifique libremente. Si desde `Main` fuera posible asignar directamente un valor al nivel, la restricción podría violarse sin que nada lo impidiera. Por eso el propio tanque debe ser el responsable de vigilar sus límites, y toda modificación debe pasar por sus métodos.

Además, el estado de operación debe corresponder a lo que realmente ocurrió: no tiene sentido que un tanque quede marcado como `LLENANDO` después de que se le solicitó detenerse.

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

| Objeto propuesto | Responsabilidad | Información que debe conservar | Comportamientos que debe realizar |
| --- | --- | --- | --- |
| Tanque | Representar un tanque de almacenamiento y controlar su nivel y estado de operación. | Identificador, capacidad máxima, nivel actual y estado de operación. | Permitir consultar su información, llenar el tanque, vaciarlo, detener su operación, consultar el nivel, obtener el porcentaje de llenado y conocer su estado. |
| Sensor de nivel | Obtener la lectura del nivel de líquido presente en un tanque. | Tanque al que está asociado y la lectura obtenida. | Obtener una lectura del nivel actual del tanque y proporcionar dicha lectura al sistema. |

El tanque es el objeto principal del sistema, ya que representa el elemento físico donde se almacena el líquido. Por esta razón, debe conservar la información relacionada con su capacidad, nivel actual, identificación y estado de operación. También debe ser responsable de las operaciones relacionadas con el llenado y vaciado del tanque.

El sensor de nivel se considera un objeto independiente porque su función principal es obtener información sobre el nivel del líquido. De esta manera, se separa la responsabilidad de almacenar y controlar el nivel de la responsabilidad de realizar la lectura.

---

## 4. Relaciones entre los objetos

El sensor de nivel necesita relacionarse con un tanque, debido a que su función depende de conocer el nivel actual del tanque que está monitoreando.

La relación principal será entre el **Sensor de nivel** y el **Tanque**. El sensor estará asociado a un tanque y podrá obtener información sobre su nivel actual para realizar la lectura correspondiente.

El tanque será responsable de conservar y modificar su propio nivel, mientras que el sensor será responsable únicamente de obtener la lectura. Por ejemplo, si un tanque tiene un nivel actual de 650 litros, el sensor podrá obtener ese valor como lectura.

Es importante mantener separadas estas responsabilidades para evitar duplicar funciones. El tanque no debe encargarse del funcionamiento del sensor y el sensor no debe modificar directamente el nivel del tanque. Cada objeto debe encargarse de la responsabilidad que le corresponde dentro del sistema.

---

## 5. Diseño de clases

A partir de los objetos identificados en la sección 2 y de las responsabilidades de la sección 3, proponemos las siguientes clases.

| Clase | Atributos propuestos | Tipo de dato | Métodos propuestos | Responsabilidad |
| --- | --- | --- | --- | --- |
| `Tanque` | `id` | `String` | `Tanque(String, double, double)` | Representar un tanque de almacenamiento, conservar su información y garantizar que su nivel permanezca siempre entre cero y su capacidad máxima. |
| | `capacidadMaxima` | `double` | `getId()`, `getCapacidadMaxima()`, `getNivelActual()`, `getEstado()` | |
| | `nivelActual` | `double` | `llenar(double)`, `vaciar(double)`, `detener()` | |
| | `estado` | `String` | `obtenerPorcentajeLlenado()`, `mostrarInformacion()` | |
| `SensorNivel` | `id` | `String` | `SensorNivel(String, Tanque)` | Medir el nivel del tanque que tiene asignado, conservar el valor de su última lectura e indicar si esa lectura es válida. |
| | `tanqueMonitoreado` | `Tanque` | `getId()`, `leer()`, `getUltimaLectura()` | |
| | `ultimaLectura` | `double` | `lecturaValida()`, `mostrarLectura()` | |
| `Main` | *(sin atributos)* | — | `main(String[])` | Crear los objetos, ejecutar la secuencia de operaciones de la simulación y presentar los resultados en consola. |

### 5.1 Atributos y encapsulación

Todos los atributos de `Tanque` y de `SensorNivel` se declaran `private`. La razón de fondo es la restricción establecida en la sección 1: si `nivelActual` fuera público, cualquier parte del programa podría asignarle un valor negativo o superior a la capacidad, y la regla `0 <= nivelActual <= capacidadMaxima` dejaría de estar garantizada. Al mantenerlo privado, el único camino para modificarlo es a través de `llenar()` y `vaciar()`, que sí validan los límites.

El mismo criterio aplica a `estado`: no ofrecemos un método que permita asignarlo libremente desde fuera, porque entonces sería posible dejar un tanque marcado como `LLENANDO` sin que se hubiera llenado nada. El estado se actualiza únicamente como consecuencia de las operaciones `llenar()`, `vaciar()` y `detener()`.

En cuanto a `capacidadMaxima`, corresponde a la construcción física del tanque y no cambia durante la operación, por lo que solo tiene método de consulta y nunca de modificación.

### 5.2 Información que se recibe por el constructor

El constructor de `Tanque` recibe el identificador, la capacidad máxima y el nivel inicial. Son los tres datos sin los cuales un tanque no puede existir de manera coherente: no tiene sentido un tanque sin nombre, sin capacidad definida o sin saber cuánto contiene al arrancar la simulación. El estado no se recibe como parámetro porque un tanque recién creado siempre inicia en `DETENIDO`; el propio constructor se encarga de asignarlo.

El constructor de `SensorNivel` recibe su identificador y una referencia al `Tanque` que va a monitorear. Un sensor sin tanque asignado no podría cumplir ninguna de sus responsabilidades, así que la relación se establece desde el momento de la creación.

### 5.3 Información consultable desde otras clases

Desde `Main` se necesita poder consultar el identificador, la capacidad, el nivel, el porcentaje y el estado del tanque para mostrarlos en pantalla. Por eso todas esas consultas se ofrecen como métodos públicos que devuelven valores sin permitir modificarlos.

Lo que no se expone en absoluto es la posibilidad de asignar directamente el nivel o el estado. Esa es precisamente la diferencia entre exponer *información* y exponer *control*: el tanque informa cómo está, pero solo él decide cómo cambia.

### 5.4 Valores del estado de operación

Como se decidió en la sección 2, el estado se maneja como texto. Para evitar errores de escritura al comparar o asignar cadenas, la clase `Tanque` definirá los tres valores como constantes públicas:

```text
public static final String DETENIDO  = "DETENIDO"
public static final String LLENANDO  = "LLENANDO"
public static final String VACIANDO  = "VACIANDO"
```

De este modo el valor se escribe una sola vez y el resto del programa se refiere a él por su nombre.

---

## 6. Diagrama UML inicial

![Diagrama UML inicial](uml-inicial.png)

El diagrama muestra las tres clases con sus atributos privados (`-`), sus constructores y sus métodos públicos (`+`).

La flecha continua de `SensorNivel` hacia `Tanque` representa una asociación: cada sensor conserva una referencia al tanque que monitorea, con multiplicidad uno a uno. Es una relación dirigida, ya que el sensor conoce al tanque pero el tanque no necesita conocer a su sensor para cumplir sus propias responsabilidades.

Las flechas punteadas desde `Main` indican dependencia: la clase principal crea instancias de ambas clases y utiliza sus métodos, pero no conserva ninguna relación estructural con ellas.

---

## 7. Justificación del diseño

### 7.1 ¿Por qué propusimos esas clases?

Nos preguntamos qué cosas del problema tienen datos propios y hacen algo con esos datos. El tanque cumple las dos: guarda su capacidad, su nivel y su estado, y además se llena, se vacía y se detiene. El sensor también: tiene su nombre, guarda lo último que midió y hace la lectura. Por eso quedaron esas dos.

Pensamos en hacer más clases pero no encontramos para qué. El estado (`DETENIDO`, `LLENANDO`, `VACIANDO`) es nada más un texto que dice cómo está el tanque, no hace nada por sí solo. Y una clase que juntara a los tres tanques tampoco la vimos necesaria, porque la práctica no pide hacer nada con todos juntos; desde `Main` los podemos manejar uno por uno sin problema.

### 7.2 ¿Cuál es la responsabilidad principal de cada clase?

El **tanque** se encarga de guardar sus datos y de cuidar que su nivel no se salga de lo permitido. Es el único que puede cambiar su nivel, y por eso también es el único que puede asegurar que nunca quede negativo ni pase de su capacidad. Esa es la parte más importante de su trabajo.

El **sensor** se encarga de medir. Le pregunta el nivel al tanque que tiene asignado, se guarda ese valor como su última lectura y dice si el valor tiene sentido o no.

El **`Main`** se encarga de armar la simulación: crea los tanques y los sensores, les pide que hagan cosas y muestra los resultados en pantalla. No hace cuentas ni revisa límites, eso ya lo hacen los otros.

### 7.3 ¿Por qué determinados atributos fueron definidos como privados?

Porque si no, se rompe la regla del problema. Si `nivelActual` fuera público, cualquiera podría escribir desde `Main` algo como:

```text
tanque.nivelActual = -50;
```

y el tanque se quedaría con menos cero litros sin que nada lo impidiera. Al ponerlo privado, la única forma de cambiarlo es usando `llenar()` o `vaciar()`, y esos métodos sí revisan los límites antes de tocar el valor. O sea que el `private` no lo pusimos porque lo pida la práctica, sino porque es lo que hace que la regla se cumpla de verdad.

Lo mismo pasa con los demás. La `capacidadMaxima` es privada y ni siquiera tiene método para cambiarla, porque el tamaño del tanque no cambia mientras está operando. El `estado` es privado porque debe decir lo que en realidad pasó; si se pudiera cambiar desde afuera, podríamos dejar un tanque que dice `LLENANDO` sin haberlo llenado. Y en el sensor, la `ultimaLectura` es privada porque tiene que venir de una medición de verdad, no de un número que le pongamos nosotros.

En resumen, la idea que seguimos fue: los objetos sí pueden decir cómo están, pero solo ellos deciden cómo cambian.

### 7.4 ¿Qué información decidimos proporcionar mediante los constructores?

Pusimos nada más lo que el objeto necesita para poder existir bien.

Al **tanque** le pasamos tres cosas: su nombre, su capacidad máxima y cuánto trae al empezar. Sin nombre no lo distinguiríamos de los otros, sin capacidad no podría revisar sus límites y sin nivel inicial no sabríamos de dónde arranca.

A propósito **no** le pasamos el estado. Un tanque que apenas se crea siempre empieza detenido, así que el constructor solito le pone `DETENIDO`. Si lo pidiéramos como parámetro, alguien podría crear un tanque que ya nace llenándose, y eso no pasa en la realidad.

Al **sensor** le pasamos dos: su nombre y el tanque que va a medir. Un sensor sin tanque no podría hacer absolutamente nada, entonces mejor que quede conectado desde que se crea. La lectura no se la pasamos porque esa tiene que salir de medir, no de que se la digamos.

### 7.5 ¿Qué objetos se relacionan entre sí y por qué?

El **sensor** se relaciona con el **tanque**: el sensor se guarda una referencia al tanque, pero el tanque no se guarda ninguna referencia al sensor.

La relación tiene que existir porque el sensor no puede medir nada si no tiene a quién medirle. Pero la hicimos en un solo sentido a propósito. El tanque hace todo su trabajo sin necesitar saber si alguien lo está midiendo, igual que un tanque real se llena y se vacía tenga o no tenga sensor puesto. Si lo hubiéramos hecho en los dos sentidos, cada uno tendría que saber del otro sin ninguna ganancia, y sería más enredado.

El `Main` usa a los dos, pero solo para crearlos y pedirles cosas, no se queda unido a ellos.

### 7.6 ¿Qué decisiones tomamos para evitar duplicar responsabilidades?

**Que el sensor no guarde el nivel del tanque.** Al principio pensamos ponerle al sensor un atributo `nivel` y actualizarlo a mano desde `Main` cada vez que el tanque cambiara. Pero eso deja el mismo dato guardado en dos lados, y si un día se nos olvida actualizarlo, el sensor diría 500 L mientras el tanque tiene 800 L, y el programa ni cuenta se daría. Así que mejor el nivel real vive solo en el tanque y el sensor se lo pregunta cada vez. Su `ultimaLectura` no es una copia del nivel, es el recuerdo de lo que midió la última vez, que no es lo mismo.

**Que el porcentaje lo calcule el tanque.** El porcentaje sale del nivel y la capacidad, y los dos son datos del tanque. Si lo calculáramos en `Main`, tendríamos que sacar esos números para hacer la cuenta afuera, y no tiene caso.

**Que `Main` no revise límites.** Toda la revisión se queda adentro del tanque. Si `Main` también revisara antes de llamar a `llenar()`, tendríamos que acordarnos de repetir esa revisión en cada lugar donde se llene un tanque, y con que se nos olvide una sola vez ya se rompió la regla.

### 7.7 ¿Qué parte del diseño fue discutida entre ambos integrantes y qué decisión tomamos?

Lo que más discutimos fue cómo conectar el sensor con el tanque.

La primera idea fue que el sensor tuviera su propio nivel guardado y que desde `Main` lo fuéramos actualizando. Se veía más fácil de programar porque las dos clases quedaban separadas y ninguna dependía de la otra.

Pero al revisarlo nos dimos cuenta de que esa separación era engañosa: en realidad nos obligaba a acordarnos de actualizar el sensor después de cada operación, y cualquier olvido daría lecturas falsas sin avisar. Aparte, tampoco se parece a como funciona de verdad, porque un sensor no espera a que alguien le diga el valor, lo saca del tanque que tiene enfrente.

Al final decidimos que el sensor guardara una referencia al tanque y que al momento de leer le pregunte el nivel directamente. Así la lectura siempre coincide con lo que el tanque tiene en ese momento y no hay forma de que se desfasen.

Lo otro que discutimos fue qué hacer cuando alguien quiere llenar de más. Vimos dos opciones: no hacer nada y rechazar la operación, o llenarlo hasta donde alcance. Escogimos la segunda porque se parece más a lo que pasa en un tanque real, donde lo que sobra simplemente se derrama. Eso ya lo dejamos anotado en la sección 1.