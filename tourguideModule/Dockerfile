FROM openjdk
EXPOSE 8080
# ADD (first part is SOURCE, second part is DESTINATION in the Docker image)
ADD build/libs/tourguideModule.jar /tourguideModule.jar
ENTRYPOINT ["java", "-jar", "/tourguideModule.jar"]
# CMD run command is: docker run -p 9090 gpsmodule