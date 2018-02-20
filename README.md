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
    
# remote :
  - docker run -d -p 5000:5000 --restart=always --name registry registry:2
  - TEST : curl -X GET http://IP:5000/v2/
  - docker push IP:5000/alpine-oraclejdk8:slim
  - docker tag frolvlad/alpine-oraclejdk8:slim IP:5000/alpine-oraclejdk8:slim
  - docker push IP:5000/alpine-oraclejdk8:slim
  - docker tag traefik IP:5000/traefik
  - docker push IP:5000/traefik
  - sudo vi /etc/docker/daemon.json
   ({
        "insecure-registries" : ["IP:5000"]
    })
  - verifier les images du registry : curl -X GET http://IP:5000/v2/_catalog
  -     add : <pushImage>true</pushImage> pour poussr l'image :
                <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <skipDockerBuild>false</skipDockerBuild>
                    <imageName>10.197.19.125:5000/${docker.image.name}</imageName>
                    <dockerDirectory>${basedir}/src/main/docker</dockerDirectory>
                    <pushImage>true</pushImage>
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                </configuration>
            </plugin>
  
# Exec

docker network create -d bridge --subnet 192.168.150.0/24 --gateway 192.168.150.1 pspe-microservice--network

cd auth-service

docker-compose up

# scale product service

docker-compose scale product=2


