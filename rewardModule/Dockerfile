FROM openjdk
EXPOSE 8081
# ADD (first part is SOURCE, second part is DESTINATION in the Docker image)
ADD build/libs/rewardModule.jar /rewardModule.jar
ENTRYPOINT ["java", "-jar", "/rewardModule.jar"]
# CMD run command is: docker run -p 9090 gpsmodule