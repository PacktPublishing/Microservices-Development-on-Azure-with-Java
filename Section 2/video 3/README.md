# Create an Azure DevOps Git Repo and Push a Containerized Spring Boot App

In case you have any issues generating a new Spring Boot project, the `SpringBootDemo` folder contains the final format of the application to be pushed to Azure DevOps.

To confirm that a docker image can be built successfully, run the following command at the project's root folder (where `pom.xml` is located):

```
docker build -f Dockerfile -t springbootdemo:latest --build-arg APP_VERSION=1 .
```
