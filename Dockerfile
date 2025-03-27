# FROM tomcat:10.1-jdk17-openjdk
# EXPOSE 8080
# ARG ARTIFACTORY_USERNAME
# ARG ARTIFACTORY_PASSWORD
# ARG WAR_FILE=CalculatorMvcProject.war
# ARG ARTIFACTORY_URL
# ARG ENVIRONMENT
# RUN curl -u ${ARTIFACTORY_USERNAME}:${ARTIFACTORY_PASSWORD} -o /usr/local/tomcat/webapps/home.war ${ARTIFACTORY_URL}/java-nagarro-assignment/binaries/${ENVIRONMENT}/${WAR_FILE}
# CMD ["catalina.sh", "run"]

FROM tomcat:7.0

# Copy WAR file
COPY ./target/*.war /usr/local/tomcat/webapps/ROOT.war

# Modify server.xml to change the port to 9090
RUN sed -i 's/8080/9090/g' /usr/local/tomcat/conf/server.xml

# Expose port 9090
EXPOSE 9090

# Start Tomcat
CMD ["catalina.sh", "run"]
