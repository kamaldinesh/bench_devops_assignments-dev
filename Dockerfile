

FROM tomcat:7.0

# Copy WAR file
COPY ./target/*.war /usr/local/tomcat/webapps/ROOT.war

# Modify server.xml to change the port to 9090
RUN sed -i 's/8080/9090/g' /usr/local/tomcat/conf/server.xml

# Expose port 9090
EXPOSE 9090

# Start Tomcat
CMD ["catalina.sh", "run"]
