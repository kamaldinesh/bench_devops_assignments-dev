FROM tomcat:10.1-jdk17-openjdk
EXPOSE 8080
ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD
ARG WAR_FILE=CalculatorMvcProject.war
ARG ARTIFACTORY_URL
ARG ENVIRONMENT
RUN curl -u ${ARTIFACTORY_USERNAME}:${ARTIFACTORY_PASSWORD} -o /usr/local/tomcat/webapps/home.war ${ARTIFACTORY_URL}/java-nagarro-assignment/binaries/${ENVIRONMENT}/${WAR_FILE}
CMD ["catalina.sh", "run"]