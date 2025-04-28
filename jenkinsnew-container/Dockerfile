# -----------------------------------------------------------------------------------
# 1) Imagen base oficial de Jenkins LTS con JDK21
# -----------------------------------------------------------------------------------
FROM jenkins/jenkins:lts-jdk21

# Cambiamos a root para instalar paquetes
USER root
    
# Evitar prompts interactivos en apt
ENV DEBIAN_FRONTEND=noninteractive
    
# -----------------------------------------------------------------------------------
# 2) Instalar utilidades (sudo, git, vim, etc.)
# -----------------------------------------------------------------------------------
RUN apt-get update && apt-get install -y --no-install-recommends \
    sudo \
    git \
    vim \
    curl \
    bzip2 \
    unzip \
    pwgen \
    ca-certificates \
 && rm -rf /var/lib/apt/lists/*
    
# Dar sudo sin contraseña a 'jenkins'
RUN echo "jenkins ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers
    
# -----------------------------------------------------------------------------------
# 3) Instalar JDK 8, JDK 11 y JDK 17 desde Temurin/Adoptium
#    (Versiones LTS y de uso libre)
# -----------------------------------------------------------------------------------
ENV JDK8_VERSION=jdk8u442-b06
ENV JDK8_RELEASE=8u442b06
ENV JDK11_VERSION=jdk-11.0.26%2B4
ENV JDK11_RELEASE=11.0.26_4
ENV JDK17_VERSION=jdk-17.0.14%2B7
ENV JDK17_RELEASE=17.0.14_7
    
RUN set -eux; \
    # --- Instalar JDK 8 ---
    curl -fsSL "https://github.com/adoptium/temurin8-binaries/releases/download/${JDK8_VERSION}/OpenJDK8U-jdk_x64_linux_hotspot_${JDK8_RELEASE}.tar.gz" \
      -o /tmp/jdk8.tgz; \
    mkdir -p /opt/jdk8; \
    tar -xzf /tmp/jdk8.tgz -C /opt/jdk8 --strip-components=1; \
    rm /tmp/jdk8.tgz; \
    \
    # --- Instalar JDK 11 ---
    curl -fsSL "https://github.com/adoptium/temurin11-binaries/releases/download/${JDK11_VERSION}/OpenJDK11U-jdk_x64_linux_hotspot_${JDK11_RELEASE}.tar.gz" \
      -o /tmp/jdk11.tgz; \
    mkdir -p /opt/jdk11; \
    tar -xzf /tmp/jdk11.tgz -C /opt/jdk11 --strip-components=1; \
    rm /tmp/jdk11.tgz; \
    \
    # --- Instalar JDK 17 ---
    curl -fsSL "https://github.com/adoptium/temurin17-binaries/releases/download/${JDK17_VERSION}/OpenJDK17U-jdk_x64_linux_hotspot_${JDK17_RELEASE}.tar.gz" \
      -o /tmp/jdk17.tgz; \
    mkdir -p /opt/jdk17; \
    tar -xzf /tmp/jdk17.tgz -C /opt/jdk17 --strip-components=1; \
    rm /tmp/jdk17.tgz; \
    \
    # Registrar JDK 8 (prioridad 1)
    update-alternatives --install /usr/bin/java  java  /opt/jdk8/bin/java  1; \
    update-alternatives --install /usr/bin/javac javac /opt/jdk8/bin/javac 1; \
    \
    # Registrar JDK 11 (prioridad 2)
    update-alternatives --install /usr/bin/java  java  /opt/jdk11/bin/java  2; \
    update-alternatives --install /usr/bin/javac javac /opt/jdk11/bin/javac 2; \
    \
    # Registrar JDK 17 (prioridad 3)
    update-alternatives --install /usr/bin/java  java  /opt/jdk17/bin/java  3; \
    update-alternatives --install /usr/bin/javac javac /opt/jdk17/bin/javac 3; \
    \
    # Registrar JDK 21 (imagen base) con prioridad 4 y dejarlo como default
    # Encontrar la ruta real de Java 21 en la imagen base
    JDK21_JAVA_PATH=$(readlink -f $(which java)); \
    JDK21_JAVAC_PATH=$(readlink -f $(which javac)); \
    echo "Found Java 21 at: $JDK21_JAVA_PATH"; \
    echo "Found Javac 21 at: $JDK21_JAVAC_PATH"; \
    update-alternatives --install /usr/bin/java  java  $JDK21_JAVA_PATH  4; \
    update-alternatives --install /usr/bin/javac javac $JDK21_JAVAC_PATH 4; \
    update-alternatives --set java $JDK21_JAVA_PATH; \
    update-alternatives --set javac $JDK21_JAVAC_PATH; \
    \
    # Mostrar las versiones finales de java (opcional)
    java -version; javac -version

# -----------------------------------------------------------------------------------
# 4) Configurar almacen keystore de Java centralizado
# ----------------------------------------------------------------------------------- 

# Creo el directorio destino para el archivo cacerts centralizado
RUN mkdir -p /etc/ssl/certs/java

# Copio el cacerts por defecto a este sitio
RUN cp -v /opt/java/openjdk/lib/security/cacerts /etc/ssl/certs/java/cacerts

# Copio el cacerts a importar
COPY config/cacerts-rhel8.p12 /var/jenkins_home/

# Importo el archivo cacerts-rhel8.p12 al keystore centralizado
RUN set -eux; \
    # Importar el archivo cacerts-rhel8.p12 al keystore centralizado
    /opt/java/openjdk/bin/keytool -importkeystore \
      -srckeystore /var/jenkins_home/cacerts-rhel8.p12 \
      -srcstoretype PKCS12 \
      -srcstorepass changeit \
      -destkeystore /etc/ssl/certs/java/cacerts \
      -deststoretype PKCS12 \
      -deststorepass changeit \
      -noprompt && \
    # Eliminar el archivo temporal
    rm /var/jenkins_home/cacerts-rhel8.p12

# Para cada instalación de Java, elimino el archivo cacerts original y creo un enlace simbólico al archivo centralizado.
RUN for cacerts in \
    /opt/jdk17/lib/security/cacerts \
    /opt/jdk8/jre/lib/security/cacerts \
    /opt/jdk11/lib/security/cacerts \
    /opt/java/openjdk/lib/security/cacerts; do \
      if [ -f "$cacerts" ]; then \
        rm -f "$cacerts" && ln -s /etc/ssl/certs/java/cacerts "$cacerts"; \
      fi; \
    done

# Verifica que cada enlace simbólico apunta al fichero correcto
RUN for cacerts in \
    /opt/jdk17/lib/security/cacerts \
    /opt/jdk8/jre/lib/security/cacerts \
    /opt/jdk11/lib/security/cacerts \
    /opt/java/openjdk/lib/security/cacerts; do \
      if [ -L "$cacerts" ]; then \
        LINKTARGET=$(readlink -f "$cacerts"); \
        if [ "$LINKTARGET" = "/etc/ssl/certs/java/cacerts" ]; then \
          echo "Verified: $cacerts -> $LINKTARGET"; \
        else \
          echo "Error: $cacerts links to $LINKTARGET (expected /etc/ssl/certs/java/cacerts)"; exit 1; \
        fi; \
      else \
        echo "Error: $cacerts is not a symbolic link"; exit 1; \
      fi; \
    done

# -----------------------------------------------------------------------------------
# 5) Instalar múltiples versiones de Maven (3.2.3, 3.6.3 y 3.8.8)
# -----------------------------------------------------------------------------------
ENV MAVEN_323_VERSION=3.2.3
ENV MAVEN_363_VERSION=3.6.3
ENV MAVEN_388_VERSION=3.8.8

RUN set -eux; \
    # --- Instalar Maven 3.2.3 ---
    curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_323_VERSION}/binaries/apache-maven-${MAVEN_323_VERSION}-bin.tar.gz" \
      -o /tmp/apache-maven-323.tar.gz; \
    mkdir -p /opt/maven-3.2.3; \
    tar -xzf /tmp/apache-maven-323.tar.gz -C /opt; \
    rm /tmp/apache-maven-323.tar.gz; \
    \
    # --- Instalar Maven 3.6.3 ---
    curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_363_VERSION}/binaries/apache-maven-${MAVEN_363_VERSION}-bin.tar.gz" \
      -o /tmp/apache-maven-363.tar.gz; \
    mkdir -p /opt/maven-3.6.3; \
    tar -xzf /tmp/apache-maven-363.tar.gz -C /opt; \
    rm /tmp/apache-maven-363.tar.gz; \
    \
    # --- Instalar Maven 3.8.8 ---
    curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_388_VERSION}/binaries/apache-maven-${MAVEN_388_VERSION}-bin.tar.gz" \
      -o /tmp/apache-maven-388.tar.gz; \
    mkdir -p /opt/maven-3.8.8; \
    tar -xzf /tmp/apache-maven-388.tar.gz -C /opt; \
    rm /tmp/apache-maven-388.tar.gz; \
    \
    # Configurar update-alternatives para Maven
    update-alternatives --install /usr/bin/mvn mvn /opt/apache-maven-${MAVEN_323_VERSION}/bin/mvn 1; \
    update-alternatives --install /usr/bin/mvn mvn /opt/apache-maven-${MAVEN_363_VERSION}/bin/mvn 2; \
    update-alternatives --install /usr/bin/mvn mvn /opt/apache-maven-${MAVEN_388_VERSION}/bin/mvn 3; \
    \
    # Establecer Maven 3.8.8 como la versión por defecto
    ln -sf /opt/apache-maven-${MAVEN_388_VERSION} /opt/maven; \
    update-alternatives --set mvn /opt/apache-maven-${MAVEN_388_VERSION}/bin/mvn; \
    \
    # Verificar la instalación
    mvn --version

ENV MAVEN_HOME=/opt/maven
ENV M2_HOME=/opt/maven
ENV PATH="$M2_HOME/bin:$PATH"

# -----------------------------------------------------------------------------------
# 6) Copiar settings.xml a una ubicación común y enlazarlo desde cada instalación de Maven
# -----------------------------------------------------------------------------------
COPY config/settings.xml /usr/local/share/maven/settings.xml
RUN for MAVEN_DIR in /opt/apache-maven-*; do \
      ln -sf /usr/local/share/maven/settings.xml "$MAVEN_DIR/conf/settings.xml"; \
    done

# -----------------------------------------------------------------------------------
# 7) Instalar un wrapper para maven v3.2.3
# -----------------------------------------------------------------------------------
# Renombra el binario original de Maven
RUN mv /opt/apache-maven-3.2.3/bin/mvn /opt/apache-maven-3.2.3/bin/mvn-original; \
cp /opt/apache-maven-3.2.3/bin/mvn-original /opt/apache-maven-3.2.3/bin/mvn323 

# Copia el wrapper que he creado
COPY scripts/mvn-323-wrapper.sh /opt/apache-maven-3.2.3/bin/mvn

# Da permisos de ejecución al wrapper
RUN chmod +x /opt/apache-maven-3.2.3/bin/mvn

# Comprobar que mvn funciona correctamente con el wrapper
RUN /opt/apache-maven-3.2.3/bin/mvn -v

# -----------------------------------------------------------------------------------
# 8) Instalar Plugin Installation Manager
# -----------------------------------------------------------------------------------
RUN curl -LfS \
    "https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/2.12.14/jenkins-plugin-manager-2.12.14.jar" \
    -o /usr/local/bin/jenkins-plugin-manager.jar
    
# -----------------------------------------------------------------------------------
# 9) Copiar e instalar plugins de la lista
# -----------------------------------------------------------------------------------
COPY plugins/plugins.txt /tmp/plugins.txt
RUN JENKINS_VERSION="$(java -jar /usr/share/jenkins/jenkins.war --version 2>/dev/null | head -n1 | sed 's/[^0-9\\.]*//g')" && \
   echo "Detected Jenkins version: ${JENKINS_VERSION}" && \
   java -jar /usr/local/bin/jenkins-plugin-manager.jar \
   --plugin-file /tmp/plugins.txt \
   --plugin-download-directory /usr/share/jenkins/ref/plugins \
   --jenkins-version ${JENKINS_VERSION} \
   --jenkins-update-center https://updates.jenkins.io/update-center.actual.json

# -----------------------------------------------------------------------------------
# 10) Mostrar la versión de Jenkins WAR incluida en la imagen base
#    y descargar jenkins-cli.jar correspondiente.
# -----------------------------------------------------------------------------------
RUN set -eux; \
    JENKINS_VERSION="$(java -jar /usr/share/jenkins/jenkins.war --version 2>/dev/null | head -n1 | sed 's/[^0-9\\.]*//g')"; \
    echo "Jenkins version detectada en la imagen base: ${JENKINS_VERSION}"; \
    # Extract the CLI jar directly from the war file using unzip instead of --extract option
    mkdir -p /tmp/jenkins-war && \
    cd /tmp/jenkins-war && \
    unzip -q /usr/share/jenkins/jenkins.war WEB-INF/lib/cli-*.jar && \
    mv WEB-INF/lib/cli-*.jar /usr/share/jenkins/jenkins-cli.jar && \
    cd / && rm -rf /tmp/jenkins-war 

# -----------------------------------------------------------------------------------
# 11) Copiar scripts Groovy de inicialización
# -----------------------------------------------------------------------------------
COPY scripts/setup_admin.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY scripts/email_config.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY scripts/tools_config.groovy /usr/share/jenkins/ref/init.groovy.d/
RUN chown -R jenkins:jenkins /usr/share/jenkins/ref/init.groovy.d
    
# -----------------------------------------------------------------------------------
# 12) Desactivar Setup Wizard usando JAVA_OPTS, sin modificar CMD / ENTRYPOINT
# -----------------------------------------------------------------------------------
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"
    
# Volver al usuario jenkins
USER jenkins
    
# La imagen base ya define ENTRYPOINT y CMD para arrancar Jenkins con su script oficial.
    