FROM openjdk
EXPOSE 8083
# ADD (first part is SOURCE, second part is DESTINATION in the Docker image)
ADD build/libs/tripPricerModule.jar /tripPricerModule.jar
ENTRYPOINT ["java", "-jar", "/tripPricerModule.jar"]
# CMD run command is: docker run -p 9090 gpsmodule