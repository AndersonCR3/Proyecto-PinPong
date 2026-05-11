Proyecto ProyectPP
===================

Descripción
-----------
Juego de PinPong en Java + Swing (PinPong/Breakout pequeño). Sigue el patrón MVP.

Requisitos
----------
- Java 21
- Maven

Compilar
--------
```bash
mvn clean compile
```

Ejecutar
-------
Desde el JAR generado (si empaquetas) o con `mvn exec`:

```bash
mvn -q -DskipTests compile
java -cp target/classes;target/dependency/* co.edu.uptc.App
```

O usando `mvn exec:java` si está configurado.

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


