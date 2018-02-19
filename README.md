# poc-spring-cloud-oauth2-jwt-angularJS
    docker pull registry
    docker run -d -p 5000:5000 --name localregistry registry
    docker pull alpine-oraclejdk8
    docker pull frolvlad/alpine-oraclejdk8:slim
    docker tag frolvlad/alpine-oraclejdk8:slim localhost:5000/alpine-oraclejdk8:slim
    docker push localhost:5000/alpine-oraclejdk8:slim
    docker pull traefik
    docker tag traefik localhost:5000/traefik
    docker push localhost:5000/traefik
  
# Exec

docker network create -d bridge --subnet 192.168.150.0/24 --gateway 192.168.150.1 pspe-microservice--network

cd auth-service

docker-compose up


