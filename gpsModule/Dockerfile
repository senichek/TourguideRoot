FROM openjdk
EXPOSE 8082
# ADD (first part is SOURCE, second part is DESTINATION in the Docker image)
ADD build/libs/GpsModule.jar /GpsModule.jar
ENTRYPOINT ["java", "-jar", "/GpsModule.jar"]
# CMD run command is: docker run -p 9090 gpsmodule