  

FROM tomcat:10.1-jdk17-openjdk

# Copy WAR file into Tomcat webapps directory
COPY ./target/*.war /usr/local/tomcat/webapps/ROOT.war

# Modify server.xml to change the port to 9091
RUN sed -i 's/8080/9090/g' /usr/local/tomcat/conf/server.xml

# Expose port 9090
EXPOSE 9090

# Start Tomcat
CMD ["catalina.sh", "run"]

