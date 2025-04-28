# 📦 Jenkins Dev Environment

Entorno de desarrollo local basado en Docker para probar diferentes versiones LTS de Jenkins y un stack completo de servicios auxiliares de CI/CD de forma rápida y reproducible.

Incluye:
- Un **contenedor Jenkins clásico LTS** (versión 2.332.2) en `jenkins-container/`.
- Un **contenedor Jenkins LTS nueva versión** (última LTS sobre OpenJDK 21) en `jenkinsnew-container/`.
- Un **contenedor de desarrollo** con **Java y Groovy** en `java-container/`.
- Servicios adicionales:
  - `artifactory-container/`  # Nexus/Artifactory para repositorios de artefactos
  - `gitea-container/`        # Gitea como servidor Git local
  - `harbor-container/`       # Harbor para registro de contenedores privado
  - `sonarqube-container/`    # SonarQube para análisis de código

---

## 🔹 1. Prerrequisitos

Antes de levantar el entorno, asegúrate de tener instaladas las siguientes herramientas:

- **Docker**: [Descargar Docker](https://www.docker.com/get-started/)
- **Docker Compose**: [Instalar Docker Compose](https://docs.docker.com/compose/install/)
- **Git** (opcional, para clonar el repositorio): [Instalar Git](https://git-scm.com/downloads)

Verifica la instalación:
```sh
docker --version
docker-compose --version
```

---

## 🔹 2. Estructura de directorios

```text
jenkins-dev-env/
├── jenkins-container/        # Jenkins LTS 2.332.2 base, plugins preconfigurados
├── jenkinsnew-container/     # Jenkins última versión LTS (OpenJDK 21)
├── java-container/           # Entorno de desarrollo Java + Groovy
├── artifactory-container/
├── gitea-container/
├── harbor-container/
├── sonarqube-container/
├── docker-compose.yml
└── README.md                 # Este fichero
``` 

---

## 🔹 3. Obtención de imágenes de Jenkins

Para los contenedores de Jenkins, puedes descargarlos directamente desde Docker Hub:

```sh
docker pull lacalvom/jenkins-dev-env-jenkins:latest
docker pull lacalvom/jenkins-dev-env-jenkinsnew:latest
```

---

## 🔹 4. (Opcional) Empaquetar e importar imágenes

Si prefieres manejar imágenes como archivos `.tar.gz` o `.zip`, aquí tienes cómo:

### Empaquetar
```sh
docker save lacalvom/jenkins-dev-env-jenkins:latest \
  | gzip > jenkins-container.tar.gz

docker save lacalvom/jenkins-dev-env-jenkinsnew:latest \
  | gzip > jenkinsnew-container.tar.gz
```
O en Zip:
```sh
docker save lacalvom/jenkins-dev-env-jenkins:latest > jenkins-container.tar
zip jenkins-container.zip jenkins-container.tar
```

### Importar
```sh
gunzip -c jenkins-container.tar.gz | docker load
docker load -i jenkins-container.tar
``` 

---

## 🔹 5. Levantar el entorno con Docker Compose

Antes de arrancar, **elige con qué Jenkins LTS** trabajar:
- **Jenkins clásico LTS** (2.332.2),
- **Jenkins última versión LTS** (OpenJDK 21).

**No utilices ambos al mismo tiempo**: en `docker-compose.yml`, comenta el bloque del servicio que no vayas a usar. Ejemplo:
```yaml
services:
  # Jenkins clásico LTS
  jenkins:
    build: jenkins-container/
    ports:
      - "8080:8080"
      - "50000:50000"

  # Jenkins última versión LTS (descomenta si la necesitas en lugar del clásico)
  # jenkinsnew:
  #   build: jenkinsnew-container/
  #   ports:
  #     - "8080:8080"
  #     - "50000:50000"

  java:
    build: java-container/
  artifactory:
    build: artifactory-container/
  gitea:
    build: gitea-container/
  harbor:
    build: harbor-container/
  sonarqube:
    build: sonarqube-container/
``` 

Arranca todo con:
```sh
docker compose up -d
```

Para detener:
```sh
docker compose down
```

Si modificas algún Dockerfile y necesitas reconstruir:
```sh
docker compose build --no-cache
```

---

## 🔹 6. Desarrollo con VS Code y DevContainers

Este repositorio incluye configuración para **VS Code DevContainers**, permitiendo editar y depurar directamente dentro del contenedor `java`.

1. Instala la extensión **DevContainers** en VS Code.
2. Abre la carpeta del proyecto:
   ```sh
   code .
   ```
3. VS Code detectará el entorno DevContainer; haz clic en **"Reopen in Container"**.
4. VS Code levantará los contenedores si no están en marcha y montará el workspace en `java`.

> **Nota:** No necesitas ejecutar `docker compose up -d` manualmente, salvo quieras verificar logs o estado antes.

---

## 🔹 7. Acceso a Jenkins

Accede siempre a la instancia activa de Jenkins en:
```
http://localhost:8080
```

Credenciales administrador preconfigurado:
```
user:  jenkins
pass:  P4ssw0rd
```

---

## 🔹 8. .gitignore recomendado

```gitignore
# Ignorar configuraciones locales y metadatos
.gitignore
**/@eaDir/

# Ignorar archivos de log, compilación y dependencias
node_modules/
*.log
dist/

# — Maven —
/target/
/pom.xml.tag
/pom.xml.releaseBackup
/pom.xml.versionsBackup
/pom.xml.next
/release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/wrapper/maven-wrapper.jar

# — Gradle —
/.gradle/
/build/
/!gradle-wrapper.jar
!gradlew
!gradlew.bat

# — IDEs comunes —
# IntelliJ IDEA
/.idea/
/*.iml
/out/

# Eclipse
/.classpath
/.project
/.settings/
/bin/

# VS Code
.vscode/

# Arquivos de registro y cobertura de tests
*.log
*.tmp
jacoco.exec
coverage/

# Artefactos empaquetados
*.jar
*.war
*.ear

```

---

## 🔹 9. Contribuciones

Si deseas proponer mejoras o reportar incidencias, crea un **issue** o un **pull request** en este mismo repositorio.

¡Gracias por usar este entorno de desarrollo Jenkins LTS! 🚀

