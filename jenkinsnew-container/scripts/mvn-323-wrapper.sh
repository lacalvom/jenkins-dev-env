#!/bin/bash
# maven-3.2.3-wrapper

# Configurar OpenJDK 8 como predeterminado
export JAVA_HOME=/opt/jdk8
export PATH=$JAVA_HOME/bin:$PATH

# Configurar Maven 3.2.3 como predeterminado
export M2_HOME="/opt/apache-maven-3.2.3"
export MAVEN_HOME="/opt/apache-maven-3.2.3"
alias mvn="mvn -s /usr/local/share/maven/settings.xml"

# Configurar el sitio del cache de Maven
export MAVEN_OPTS="-Dmaven.repo.local=/var/jenkins_home/.m2/repository"
export PATH=$M2_HOME/bin:$PATH:/usr/bin/git

# Ejecuta Maven 3.2.3 con los parámetros que se le pasen
/opt/apache-maven-3.2.3/bin/mvn323 "$@"
RESULTADO=$?

# Restaura la configuración original del entorno
export JAVA_HOME=/opt/java/openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Configurar Maven 3.8.8 como predeterminado
export M2_HOME="/opt/maven"
export MAVEN_HOME="/opt/maven"
alias mvn="mvn -s /usr/local/share/maven/settings.xml"

# Configurar el sitio del cache de Maven
export MAVEN_OPTS="-Dmaven.repo.local=/var/jenkins_home/.m2/repository"
export PATH=$M2_HOME/bin:$PATH:/usr/bin/git

exit $RESULTADO
