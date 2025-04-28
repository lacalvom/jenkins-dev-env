FROM docker.bintray.io/jfrog/artifactory-oss:7.77.7

# Expose necessary ports
EXPOSE 8082

# Set environment variables for a simple standalone installation
ENV ARTIFACTORY_HOME=/var/opt/jfrog/artifactory

# Define the working directory
WORKDIR $ARTIFACTORY_HOME

# Start Artifactory
CMD ["/opt/jfrog/artifactory/app/bin/artifactory.sh"]
