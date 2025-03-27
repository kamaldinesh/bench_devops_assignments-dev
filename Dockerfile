

FROM tomcat:7.0

# Copy WAR file
COPY ./target/*.war /usr/local/tomcat/webapps/ROOT.war

# Modify server.xml to change the port to 9091
RUN sed -i 's/8080/9091/g' /usr/local/tomcat/conf/server.xml

# Expose port 9091
EXPOSE 9091

# Start Tomcat
CMD ["catalina.sh", "run"]
