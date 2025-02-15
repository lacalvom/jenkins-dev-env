import jenkins.model.*

println "--> Configurando correo de administración..."

def instance = Jenkins.getInstance()
def jenkinsLocationConfiguration = instance.getDescriptor("jenkins.model.JenkinsLocationConfiguration")

jenkinsLocationConfiguration.setAdminAddress("Jenkins Admin <luis.calvo@gft.com>")
jenkinsLocationConfiguration.save()
