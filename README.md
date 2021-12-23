# TourGuide Application

## Follow these steps to run this app in Docker:
1. Download, install and run **Docker**: https://docs.docker.com/get-docker/
2. Clone or download this project
3. Open the Terminal (for Windows press "Windows + R", type CMD and press Enter)
4. Navigate to the project's folder and run the file called "docker-compose.yml" by typing the following command and pressing Enter:\
   **docker-compose up**\
   For example:\
   *C:/TourguideRoot/docker-compose up*


## Several URLs for testing:
http://localhost:8080/users \
http://localhost:8080/getAllLocations?userName=internalUser2 \
http://localhost:8080/setProximityBuffer?value=10 \
http://localhost:8080/getNearbyAttractions?userName=internalUser2 \
http://localhost:8080/getTripDeals?userName=internalUser2 \
http://localhost:8080/getUserPreferences?userName=internalUser2 \
http://localhost:8080/getAllCurrentLocations
