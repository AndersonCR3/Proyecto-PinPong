Proyecto ProyectPP
===================

Descripción
-----------
Juego de PinPong en Java + Swing (PinPong/Breakout pequeño). Sigue el patrón MVP.

Requisitos
----------
- Java 17 o superior
- Maven

Compilar
--------
```bash
mvn clean compile
```

Ejecutar
-------
Opcion recomendada (Windows y Linux):

```bash
mvn clean compile
mvn exec:java
```

Si deseas ejecutar manualmente por classpath, usa el separador correcto por sistema operativo.

Windows (PowerShell/cmd):

```bash
java -cp target/classes;target/dependency/* co.edu.uptc.App
```

Linux/macOS (bash/zsh):

```bash
java -cp target/classes:target/dependency/* co.edu.uptc.App
```

Nota: tambien puedes usar la ejecucion manual por classpath mostrada arriba.

Notas
-----
- El punto de entrada es la clase `co.edu.uptc.App`, que delega a `ApplicationLauncher`.
- Los logs de errores se escriben en `logs/proyectpp.log` (configurado por `AppLogger`).
- Para empujar a GitHub: asegúrate de sincronizar con `origin/main` antes de `git push`.

Contacto
-------
Autor: Anderson Carreño
Correo: anderson.carreno@uptc.edu.co
Institucion: UPTC
Materia: Programacion 3


