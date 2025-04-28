FROM openjdk:8-jdk

# usuario root
USER root

# Variables de entorno
ENV JENKINS_VERSION=2.332.2
ENV JENKINS_HOME=/var/jenkins_home
ENV JENKINS_UC=https://updates.jenkins.io

# Creo el usuario y el grupo 'jenkins'
RUN groupadd -g 1000 jenkins \
  && useradd -d "$JENKINS_HOME" -u 1000 -g 1000 -m -s /bin/bash jenkins

# Instalacion de utilidades (sudo, vim, etc.)
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl sudo vim tar git bzip2 maven gradle unzip openjdk-11-jdk \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Agregar repositorio bullseye-backports para OpenJDK 17
RUN echo "deb http://deb.debian.org/debian bullseye-backports main" \
    >> /etc/apt/sources.list.d/backports.list

# Instalar OpenJDK 17 desde bullseye-backports
RUN apt-get update && apt-get install -y --no-install-recommends \
    -t bullseye-backports openjdk-17-jdk \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Agrego el usuario jenkins al grupo sudo (por si quieres usar sudo dentro del contenedor)
RUN usermod -aG sudo jenkins

# Descargo el WAR de Jenkins en /usr/share/jenkins/
WORKDIR /usr/share/jenkins
RUN curl -fsSL ${JENKINS_UC}/download/war/${JENKINS_VERSION}/jenkins.war -o jenkins.war

# Copio automáticamente los plugins de una carpeta local "plugins"
# que contiene todos los .hpi/.jpi.
RUN mkdir -p $JENKINS_HOME/plugins
COPY plugins/ $JENKINS_HOME/plugins/

# Ajusto los permisos para el usuario jenkins
RUN chown -R jenkins:jenkins /usr/share/jenkins ${JENKINS_HOME}

# Copio jenkins-cli.jar y jenkins-plugin-cli.jar
RUN mkdir -p /usr/share/jenkins/tools
COPY tools/jenkins-cli.jar /usr/share/jenkins/tools/jenkins-cli.jar
COPY tools/jenkins-plugin-cli.jar /usr/share/jenkins/tools/jenkins-plugin-cli.jar
RUN chown -R jenkins:jenkins /usr/share/jenkins/tools/ && \
    chmod +x /usr/share/jenkins/tools/*.jar

## Copio los scripts de configuración
RUN mkdir -p $JENKINS_HOME/init.groovy.d
COPY scripts/setup_admin.groovy $JENKINS_HOME/init.groovy.d/
COPY scripts/email_config.groovy $JENKINS_HOME/init.groovy.d/
RUN chown -R jenkins:jenkins $JENKINS_HOME/init.groovy.d

# Copiar config.xml personalizado
COPY config/config.xml ${JENKINS_HOME}

# Cambiar a usuario jenkins
USER jenkins

# Arranco Jenkins con el .war
CMD ["java", "-Djenkins.install.runSetupWizard=false", "-DJENKINS_HOME=/var/jenkins_home", "-jar", "/usr/share/jenkins/jenkins.war"]
