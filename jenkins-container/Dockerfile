FROM jenkins/jenkins:2.332.2-lts

USER root
# actualizar los origenes de software e instalar diversas herramientas
RUN apt update && apt install -y git maven gradle unzip curl vim openjdk-11-jre

# Instalar dependencias para RVM y Ruby
RUN apt-get update && apt-get install -y gpg gcc make tar bzip2     && curl -sSL https://rvm.io/mpapis.asc | gpg --import -     && curl -sSL https://rvm.io/pkuczynski.asc | gpg --import -     && curl -sSL https://get.rvm.io | bash -s stable     && /bin/bash -c "source /usr/local/rvm/scripts/rvm && rvm install 2.1.9 && rvm use 2.1.9 --default"

# Verificar instalación de Ruby
RUN /bin/bash -c "source /usr/local/rvm/scripts/rvm && ruby -v"

# Crear carpeta de herramientas dentro del contenedor
RUN mkdir -p /usr/share/jenkins/tools

# Copiar jenkins-cli.jar y jenkins-plugin-cli.jar
COPY tools/jenkins-cli.jar /usr/share/jenkins/tools/jenkins-cli.jar
COPY tools/jenkins-plugin-cli.jar /usr/share/jenkins/tools/jenkins-plugin-cli.jar

# Copiar los scripts de configuración (usuario admin, correo, permisos, etc.)
RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d
COPY scripts/setup_admin.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY scripts/email_config.groovy /usr/share/jenkins/ref/init.groovy.d/
RUN chown -R jenkins:jenkins /usr/share/jenkins/ref/init.groovy.d

# Desactivar el asistente de configuración inicial
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Copiar plugins predescargados para que Jenkins los instale en el primer arranque
RUN mkdir -p /usr/share/jenkins/ref/plugins
COPY plugins/ /usr/share/jenkins/ref/plugins/
RUN chown -R jenkins:jenkins /usr/share/jenkins/ref/plugins

USER jenkins
# Ejecutar la instalación de plugins antes de arrancar Jenkins
CMD ["/usr/local/bin/jenkins.sh"]
