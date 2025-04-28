import jenkins.model.*
import hudson.model.*
import hudson.tools.*
import hudson.tasks.Maven.MavenInstallation
import hudson.tasks.Maven
import hudson.tasks.Ant.AntInstallation
import hudson.tasks.Ant
import hudson.plugins.gradle.GradleInstallation
import hudson.plugins.gradle.GradleInstaller
import hudson.tools.InstallSourceProperty
import hudson.tools.ToolProperty
import hudson.tools.ToolPropertyDescriptor
import hudson.util.DescribableList
import jenkins.mvn.GlobalMavenConfig
import jenkins.mvn.FilePathGlobalSettingsProvider

println "--> Configurando herramientas JDK y Maven..."

// Get Jenkins instance
def jenkins = Jenkins.getInstance()

// Configure JDK installations
def jdkDescriptor = jenkins.getDescriptor("hudson.model.JDK")
def jdkInstallations = []

// Add JDK 8
def jdk8 = new JDK("JDK 8", "/opt/jdk8")
jdkInstallations.add(jdk8)

// Add JDK 11
def jdk11 = new JDK("JDK 11", "/opt/jdk11")
jdkInstallations.add(jdk11)

// Add JDK 17
def jdk17 = new JDK("JDK 17", "/opt/jdk17")
jdkInstallations.add(jdk17)

// Find JDK 21 in the default location
def jdk21 = new JDK("JDK 21", System.getProperty("java.home"))
jdkInstallations.add(jdk21)

// Set JDK installations
jdkDescriptor.setInstallations(jdkInstallations.toArray(new JDK[0]))

// Configure Maven installations
def mavenDescriptor = jenkins.getDescriptor("hudson.tasks.Maven")
def mavenInstallations = []

// Add Maven installations
def maven323 = new MavenInstallation("Maven 3.2.3", "/opt/apache-maven-3.2.3", [])
mavenInstallations.add(maven323)

def maven363 = new MavenInstallation("Maven 3.6.3", "/opt/apache-maven-3.6.3", [])
mavenInstallations.add(maven363)

def maven388 = new MavenInstallation("Maven 3.8.8", "/opt/apache-maven-3.8.8", [])
mavenInstallations.add(maven388)

// Set Maven installations
mavenDescriptor.setInstallations(mavenInstallations.toArray(new MavenInstallation[0]))

// Configure Global Maven Settings
def globalSettingsPath = "/usr/local/share/maven/settings.xml"

// Set Maven global settings using both approaches for compatibility
def globalMavenSettings = new FilePathGlobalSettingsProvider(globalSettingsPath)
def globalMavenConfig = GlobalMavenConfig.get()
globalMavenConfig.setGlobalSettingsProvider(globalMavenSettings)

// Save configuration
jenkins.save()

println "--> Configuración de herramientas completada."

