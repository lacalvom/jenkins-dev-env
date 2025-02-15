# 📦 Jenkins Dev Environment

Este entorno proporciona un contenedor con Jenkins **2.332.2** preconfigurado con plugins y otro contenedor de desarrollo con **Java y Groovy**. 

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
│── jenkins-container.tar.gz    # imagen del contenedor de jenkins para importar
│── java-container.tar.gz       # imagen del contenedor de java para importar
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

## 🔹 **5. Acceder a Jenkins**
Una vez que el entorno esté en ejecución, accede a Jenkins en **http://localhost:8080**

La contraseña del usuario administrador preconfirado `jenkins`, es: `P4ssw0rd`

---

## 🔹 **6. Conclusión**
✅ **Este entorno preconfigurado permite comenzar sin necesidad de instalar Jenkins manualmente.**  
✅ **Todas las imágenes Docker están listas para ser importadas y ejecutadas con `docker compose up -d`.**  
✅ **Incluye Java y Groovy, asegurando un entorno completo de desarrollo.**  

🚀 **¡Con estas instrucciones, cualquier desarrollador podrá levantar el mismo entorno en minutos!**

