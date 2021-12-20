FROM openjdk
EXPOSE 8084
# ADD (first part is SOURCE, second part is DESTINATION in the Docker image)
ADD build/libs/rewardCentralModule.jar /rewardCentralModule.jar
ENTRYPOINT ["java", "-jar", "/rewardCentralModule.jar"]
# CMD run command is: docker run -p 9090 gpsmodule