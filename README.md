# CalculatorMvcProject - DevOps Pipeline

This project implements a CI/CD pipeline to build, test, analyze, and deploy a Maven-based Java web application (CalculatorMvcProject). The pipeline supports two environments (dev and prod), uploads artifacts to Artifactory, builds Docker images, pushes them to Docker Hub, and runs the application in a container.

## Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Pipeline Stages](#pipeline-stages)
- [Setup Instructions](#setup-instructions)
  - [1. Set Up the Environment](#1-set-up-the-environment)
  - [2. Configure Artifactory](#2-configure-artifactory)
  - [3. Configure SonarQube](#3-configure-sonarqube)
  - [4. Configure Docker](#4-configure-docker)
  - [5. Configure GitLab](#5-configure-gitlab)
  - [6. Configure Docker Hub](#6-configure-docker-hub)
- [Running the Pipeline](#running-the-pipeline)
- [Troubleshooting](#troubleshooting)
- [Known Issues](#known-issues)
- [Contributing](#contributing)
- [License](#license)

## Overview

The CalculatorMvcProject is a Java web application built with Maven and Spring Boot. The CI/CD pipeline automates the following tasks:
- Checks out code from GitLab (dev or prod branch).
- Builds the application using Maven.
- Runs unit tests and generates test reports.
- Performs static code analysis with SonarQube.
- Uploads the WAR file to Artifactory.
- Builds a Docker image using the WAR file.
- Pushes the Docker image to Docker Hub.
- Runs the application in a container, exposing it on a specified port (8000 for dev, 8086 for prod).

The pipeline is designed to be environment-agnostic, supporting dev and prod environments with isolated artifacts. It can be run manually or adapted to a CI/CD tool like GitLab CI or GitHub Actions.

## Prerequisites

- **Git**: For version control and repository access.
- **Maven**: For building the Java application (version 3.8.x or later).
- **GitLab**: A GitLab repository with dev and prod branches (e.g., `https://gitlab.com/nagarro-devops1/bench_devops_assignments`).
- **Artifactory**: An Artifactory server for storing artifacts (e.g., `http://<WSL_IP>:8081/artifactory`).
- **SonarQube**: A SonarQube server for code analysis (e.g., `http://<WSL_IP>:9000`).
- **Docker**: Docker installed on the build machine, with BuildKit enabled.
- **Docker Hub**: A Docker Hub account (`docker-username`) with repositories for `dev-<BUILD_NUMBER>` and `prod-<BUILD_NUMBER>`.
- **WSL (Optional)**: If running on Windows with WSL, ensure Docker Desktop is configured with WSL integration.

## Project Structure

```
CalculatorMvcProject/
├── src/                    # Source code for the Java application
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── Dockerfile              # Dockerfile for building the Docker image
├── pom.xml                 # Maven configuration file
├── README.md               # This file
└── scripts/                # Scripts for running the pipeline manually (optional)
    ├── build.sh            # Script for building and testing
    ├── deploy.sh           # Script for deploying to Docker
```

## Pipeline Stages

- **Checkout**: Checks out the specified branch (dev or prod) from GitLab.
- **Build-Maven**: Builds the application using Maven (`mvn clean package`).
- **Unit-Test**: Runs unit tests with Maven (`mvn test`) and generates test reports.
- **SonarQube-Analysis**: Performs static code analysis using SonarQube.
- **Artifactory**: Uploads the WAR file to Artifactory under an environment-specific path (`java-nagarro-assignment/binaries/<ENVIRONMENT>/`).
- **Build-Docker**: Builds a Docker image using the WAR file from Artifactory.
- **Docker-Push**: Pushes the Docker image to Docker Hub (`dpcode72/<ENVIRONMENT>-<BUILD_NUMBER>:<VERSION>`).
- **Run-container**: Runs the Docker container, mapping the appropriate port (8000 for dev, 8086 for prod).
- **Provide-URL**: Prints the application URL (e.g., `http://<WSL_IP>:8000` for dev).

## Setup Instructions

### 1. Set Up the Environment

**Install Git:**
On WSL Ubuntu:
```bash
sudo apt update
sudo apt install git
```
Configure Git with your credentials:
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

**Install Maven:**
On WSL Ubuntu:
```bash
sudo apt install maven
mvn --version
```

### 2. Configure Artifactory

**Set Up Artifactory:**
Run Artifactory using Docker:
```bash
docker run -d --name artifactory -p 8081:8081 releases-docker.jfrog.io/jfrog/artifactory-oss:latest
```
Access Artifactory at `http://<WSL_IP>:8081/artifactory` (default credentials: `admin/password`).

**Create a Repository:**
- In Artifactory, create a generic repository named `java-nagarro-assignment`.
- Prepare Artifactory credentials (e.g., `admin/password`) for use in the pipeline scripts.

### 3. Configure SonarQube

**Set Up SonarQube:**
Run SonarQube using Docker:
```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest
```
Access SonarQube at `http://<WSL_IP>:9000` (default credentials: `admin/admin`).

**Generate a SonarQube Token:**
- Log in to SonarQube, go to *My Account > Security*, and generate a token.
- Save this token for use in the pipeline scripts.

### 4. Configure Docker

**Install Docker:**
On WSL Ubuntu:
```bash
sudo apt update
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $(whoami)
```
If using WSL, ensure Docker Desktop is running with WSL integration enabled.

**Enable BuildKit:**
Set the `DOCKER_BUILDKIT` environment variable:
```bash
export DOCKER_BUILDKIT=1
```

### 5. Configure GitLab

**Set Up the Repository:**
Ensure the repository `https://gitlab.com/nagarro-devops1/bench_devops_assignments` has `dev` and `prod` branches. If not, create them:
```bash
git clone https://gitlab.com/nagarro-devops1/bench_devops_assignments.git
cd bench_devops_assignments
git checkout -b dev
git push origin dev
git checkout -b prod
git push origin prod
```

### 6. Configure Docker Hub

**Create Repositories:**
- Log in to Docker Hub (`https://hub.docker.com`) with your username (`dpcode72`).
- Repositories will be created automatically when you push images (e.g., `dpcode72/dev-<BUILD_NUMBER>`).

**Generate a Personal Access Token (PAT):**
- Go to *Account Settings > Security > Personal Access Tokens*.
- Generate a token with *Read, Write, Delete* permissions.
- Save this token for use in the pipeline scripts.

## Running the Pipeline

This section provides a manual script-based approach to running the pipeline. You can adapt this to a CI/CD tool like GitLab CI or GitHub Actions by creating a pipeline configuration file (e.g., `.gitlab-ci.yml`).

### 1. Clone the Repository
Clone the repository and check out the desired branch:
```bash
git clone https://gitlab.com/nagarro-devops1/bench_devops_assignments.git
cd bench_devops_assignments
git checkout dev  # or prod
```

### 2. Set Environment Variables
Set the necessary environment variables:
```bash
export ENVIRONMENT="dev"  # or "prod"
export WSL_IP="172.30.158.7"
export VERSION="2.1"
export BUILD_NUMBER="22"  # Replace with your build number
export IMAGE_NAME="dpcode72/${ENVIRONMENT}-${BUILD_NUMBER}"
export IMAGE_TAG="${VERSION}"
export PORT=$([ "$ENVIRONMENT" == "dev" ] && echo "8000" || echo "8086")
export APP_URL="http://${WSL_IP}:${PORT}"
export SONARQUBE_URL="http://${WSL_IP}:9000"
export ARTIFACTORY_URL="http://${WSL_IP}:8081"
export ARTIFACTORY_USERNAME="admin"
export ARTIFACTORY_PASSWORD="password"  # Replace with your Artifactory password
export SONAR_TOKEN="your-sonarqube-token"  # Replace with your SonarQube token
export DOCKER_USERNAME="docker-username"
export DOCKER_PASSWORD="your-docker-hub-pat"  # Replace with your Docker Hub PAT
```

### 3. Create the Dockerfile
Ensure the `Dockerfile` exists in your repository with the following content:
```dockerfile
FROM tomcat:10.1-jdk17-openjdk
EXPOSE 8080
ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD
ARG WAR_FILE=CalculatorMvcProject.war
ARG ARTIFACTORY_URL
ARG ENVIRONMENT
RUN curl -u ${ARTIFACTORY_USERNAME}:${ARTIFACTORY_PASSWORD} -o /usr/local/tomcat/webapps/home.war ${ARTIFACTORY_URL}/java-nagarro-assignment/binaries/${ENVIRONMENT}/${WAR_FILE}
CMD ["catalina.sh", "run"]
```

### 4. Run the Pipeline Stages
Run each stage manually using shell commands:

**Build-Maven:**
```bash
mvn clean package
```

**Unit-Test:**
```bash
mvn test
# Test reports are in target/surefire-reports/
```

**SonarQube-Analysis:**
```bash
mvn sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=${SONAR_TOKEN}
```

**Artifactory:**
```bash
curl -u ${ARTIFACTORY_USERNAME}:${ARTIFACTORY_PASSWORD} -T target/CalculatorMvcProject.war "${ARTIFACTORY_URL}/artifactory/java-nagarro-assignment/binaries/${ENVIRONMENT}/CalculatorMvcProject.war"
```

**Build-Docker:**
```bash
docker build --build-arg ARTIFACTORY_USERNAME=${ARTIFACTORY_USERNAME} \
             --build-arg ARTIFACTORY_PASSWORD=${ARTIFACTORY_PASSWORD} \
             --build-arg ARTIFACTORY_URL=${ARTIFACTORY_URL} \
             --build-arg ENVIRONMENT=${ENVIRONMENT} \
             -t ${IMAGE_NAME}:${IMAGE_TAG} .
```

**Docker-Push:**
```bash
echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
docker push ${IMAGE_NAME}:${IMAGE_TAG}
```

**Run-container:**
```bash
docker stop endpointapi-${ENVIRONMENT} || true
docker rm endpointapi-${ENVIRONMENT} || true
docker run -d --name endpointapi-${ENVIRONMENT} -p ${PORT}:8080 ${IMAGE_NAME}:${IMAGE_TAG}
```

**Provide-URL:**
```bash
if [ "${ENVIRONMENT}" == "dev" ]; then
    echo "Dev URL: ${APP_URL}"
else
    echo "Prod URL: ${APP_URL}"
fi
```

### 5. Verify the Deployment
- Access the application:
  - Dev: `http://<WSL_IP>:8000`
  - Prod: `http://<WSL_IP>:8086`
- Check Artifactory for the uploaded WAR file:
  - `java-nagarro-assignment/binaries/dev/CalculatorMvcProject.war`
  - `java-nagarro-assignment/binaries/prod/CalculatorMvcProject.war`
- Check Docker Hub for the pushed image:
  - `dpcode72/dev-<BUILD_NUMBER>:<VERSION>`
  - `dpcode72/prod-<BUILD_NUMBER>:<VERSION>`

## Troubleshooting

- **Wrong Environment Used:**
  - Verify the `ENVIRONMENT` variable is set correctly (`dev` or `prod`).
  - Ensure the corresponding branch exists in the GitLab repository.
- **Artifactory Upload Fails:**
  - Check the Artifactory credentials (`ARTIFACTORY_USERNAME` and `ARTIFACTORY_PASSWORD`).
  - Ensure the Artifactory server is running at `http://<WSL_IP>:8081/artifactory`.
- **SonarQube Analysis Fails:**
  - Verify the `SONAR_TOKEN` is correct.
  - Ensure SonarQube is running at `http://<WSL_IP>:9000`.
- **Docker Push Fails with "denied: requested access to the resource is denied":**
  - Verify the Docker Hub credentials (`DOCKER_USERNAME` and `DOCKER_PASSWORD`).
  - Test manually:
    ```bash
    echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
    docker push ${IMAGE_NAME}:${IMAGE_TAG}
    ```
- **Docker Build Fails to Download WAR File:**
  - Check Artifactory to ensure the WAR file exists in the correct path.
  - Verify the `ENVIRONMENT` variable is set correctly.
- **Container Fails to Start:**
  - Check the container logs:
    ```bash
    docker logs endpointapi-<ENVIRONMENT>
    ```
  - Ensure the port (8000 for dev, 8086 for prod) is not already in use.

## Known Issues

- **Deprecated Docker Builder Warning:**
  - The pipeline uses `DOCKER_BUILDKIT=1` to suppress the warning. If it persists, ensure BuildKit is enabled:
    ```bash
    export DOCKER_BUILDKIT=1
    ```
- **Artifactory Overwrites:**
  - Fixed by uploading artifacts to environment-specific paths.

## Contributing

- Fork the repository.
- Create a feature branch (`git checkout -b feature/your-feature`).
- Commit your changes (`git commit -m "Add your feature"`).
- Push to the branch (`git push origin feature/your-feature`).
- Create a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
