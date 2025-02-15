# 📦 Jenkins Dev Environment

Entorno de desarrollo con un contenedor Jenkins **2.332.2** preconfigurado con plugins y otro contenedor de **Java y Groovy**. 

Incluye:
- Un **contenedor Jenkins** con los plugins necesarios.
- Un **contenedor de desarrollo** con **Java y Groovy**.
- Todos los archivos de configuración y scripts requeridos para la instalación y configuración automática.

De esta forma se podrá levantar este entorno sin necesidad de reconstruir las imágenes desde cero, simplemente importando las imágenes preconfiguradas y ejecutando los comandos adecuados.

---

## 🔹 **1. Prerrequisitos**
Antes de instalar el entorno, asegúrate de tener instaladas las siguientes herramientas:

- **Docker**: [Descargar Docker](https://www.docker.com/get-started/)
- **Docker Compose**: [Instalar Docker Compose](https://docs.docker.com/compose/install/)
- **Git** (Opcional, si necesitas clonar el repositorio): [Instalar Git](https://git-scm.com/downloads)

Verifica que Docker y Docker Compose están correctamente instalados ejecutando:

```sh
docker --version
docker-compose --version
```

Si ambos comandos devuelven un número de versión, puedes continuar.

---

## 🔹 **2. Estructura de directorios y archivos importantes**

```
jenkins-dev-env/
│── jenkins-container/
│   │── Dockerfile
│   │── scripts/
│   │── plugins/
│   │── ...
│── java-container/
│   │── Dockerfile
│   │── ...
│── docker-compose.yml
│── README.md
```

---

## 🔹 **3. Instalación del Entorno**

### 📦 **Descarga de Archivos Grandes**

Para evitar limitaciones de GitHub, los archivos grandes deben descargarse manualmente desde Google Drive.

### 📌 **Descarga los Archivos Necesarios:**
- 🔗 [Jenkins Container](https://drive.google.com/uc?export=download&id=1gL-4kWeMF-v6sW-6rvbBjmFx1ZeAz2JN)
- 🔗 [Java Container](https://drive.google.com/uc?export=download&id=1AH4bwPAlgReyytN1wJ0bF6HpSAg-Nhcb)

### ✅ **Importar las Imágenes de Docker**
Para evitar que cada desarrollador tenga que construir las imágenes manualmente, estas ya han sido exportadas y comprimidas. Para importarlas en Docker:

#### **Linux/macOS**
```sh
gunzip -c jenkins-container.tar.gz | docker load
gunzip -c java-container.tar.gz | docker load
```

Si prefieres descomprimir antes:
```sh
tar -xvf jenkins-container.tar.gz
docker load -i jenkins-container.tar
```

Para verificar que las imágenes han sido importadas correctamente:
```sh
docker images
```

#### **Windows (PowerShell)**
```powershell
tar -xvf jenkins-container.tar.gz
docker load -i jenkins-container.tar
```

---

## 🔹 **4. Levantar el Entorno con Docker Compose**
Una vez que las imágenes han sido importadas, levanta el entorno ejecutando:

```sh
docker compose up -d
```

Para detener el entorno cuando no se necesite:
```sh
docker compose down
```

Si fuera necesario reconstruir las imagenes:
```sh
docker compose build --no-cache
```

---

## 🔹 **5. Desarrollo con VS Code y DevContainers**
El entorno está configurado para integrarse con **VS Code DevContainers**, lo que permite trabajar dentro del contenedor de desarrollo con **Java y Groovy**.

### ✅ **Abrir el Entorno en VS Code**
1. **Asegúrate de tener instalada la extensión de DevContainers** en VS Code.
2. Abre el proyecto en VS Code:
   ```sh
   code jenkins-dev-env
   ```
3. **VS Code detectará automáticamente el entorno DevContainer.**
4. Haz clic en **“Reopen in Container”** cuando aparezca la notificación.
5. **No es necesario ejecutar `docker compose up -d` manualmente**, ya que VS Code levantará automáticamente los contenedores definidos en `docker-compose.yml` si aún no están en ejecución.
6. Si los contenedores ya estaban corriendo, VS Code simplemente se conectará a ellos sin reiniciarlos.

### 🔹 **¿Cuándo ejecutar `docker compose up -d` manualmente?**
| Situación | ¿Ejecutar `docker compose up -d` antes? | Notas |
|-----------|-----------------------------------|-------|
| **Primera vez que abres en VS Code** | ❌ No es necesario | VS Code levantará los contenedores automáticamente. |
| **Los contenedores ya están corriendo** | ❌ No es necesario | VS Code solo se conectará al contenedor sin reiniciarlo. |
| **Quieres asegurarte de que todo funciona antes de abrir VS Code** | ✅ Sí, puedes ejecutar `docker compose up -d` manualmente | Útil para verificar logs antes de abrir VS Code. |

Para más información sobre DevContainers, consulta la documentación oficial de VS Code: [https://code.visualstudio.com/docs/devcontainers/containers](https://code.visualstudio.com/docs/devcontainers/containers)

---

## 🔹 **6. Acceder a Jenkins**
Una vez que el entorno esté en ejecución, accede a Jenkins en **http://localhost:8080**

La contraseña del usuario administrador preconfigurado `jenkins`, es: `P4ssw0rd`

---

## 🔹 **7. Conclusión**
✅ **Este entorno preconfigurado permite comenzar sin necesidad de instalar Jenkins manualmente.**  
✅ **Todas las imágenes Docker están listas para ser importadas y ejecutadas con `docker compose up -d`.**  
✅ **Incluye Java y Groovy, asegurando un entorno completo de desarrollo.**  

🚀 **¡Con estas instrucciones, cualquier desarrollador podrá levantar el mismo entorno en minutos!**

