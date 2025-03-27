pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'prod'], description: 'Select the environment to deploy to')
        string(name: 'WSL_IP', defaultValue: '', description: 'Enter the WSL IP address (e.g., 172.17.0.1)')
        string(name: 'VERSION', defaultValue: '1.0', description: 'Enter the version tag for the Docker image (e.g., 1.2)')
    }
    
    environment {
        IMAGE_NAME = "dpcode72/${params.ENVIRONMENT}-${BUILD_NUMBER}"
        IMAGE_TAG = "${params.VERSION}"
        PORT = "${params.ENVIRONMENT == 'dev' ? '8000' : '8086'}"
        APP_URL = "http://${params.WSL_IP}:${PORT}"
        SONARQUBE_URL = "http://${params.WSL_IP}:9000"
        ARTIFACTORY_URL = "http://${params.WSL_IP}:8081"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', 
                          branches: [[name: "*/${params.ENVIRONMENT}"]], 
                          userRemoteConfigs: [[url: 'https://gitlab.com/nagarro-devops1/bench_devops_assignments.git', 
                                              credentialsId: 'Gitlab-Access-1']]])
            }
        }
        
        stage('Build-Maven') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('Unit-Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube-Analysis') {
            steps {
                script {
                    echo "Running SonarQube analysis with URL: ${SONARQUBE_URL}"
                    withSonarQubeEnv(credentialsId: 'sonarqube-token', installationName: 'SonarQube') {
                        sh 'mvn sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=$SONAR_TOKEN'
                    }
                }
            }
        }
        
        stage('Artifactory') {
            steps {
                script {
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "target/*.war",
                                "target": "java-nagarro-assignment/binaries/${params.ENVIRONMENT}/"
                            }
                        ]
                    }"""
                    rtUpload serverId: 'artifactory-server', spec: uploadSpec
                    rtPublishBuildInfo serverId: 'artifactory-server'
                }
            }
        }
        
        stage('Build-Docker') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'Artifactory-Auth', usernameVariable: 'ARTIFACTORY_USERNAME', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
                    sh "docker build --build-arg ARTIFACTORY_USERNAME=\${ARTIFACTORY_USERNAME} --build-arg ARTIFACTORY_PASSWORD=\${ARTIFACTORY_PASSWORD} --build-arg ARTIFACTORY_URL=${ARTIFACTORY_URL}/artifactory --build-arg ENVIRONMENT=${params.ENVIRONMENT} -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }
        
        stage('Docker-Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-login') {
                        docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
                    }
                }
            }
        }
        
        stage('Run-container') {
            steps {
                script {
                    sh "docker stop endpointapi-${params.ENVIRONMENT} || true"
                    sh "docker rm endpointapi-${params.ENVIRONMENT} || true"
                    sh "docker run -d --name endpointapi-${params.ENVIRONMENT} -p ${PORT}:8080 ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }
        
        stage('Provide-URL') {
            steps {
                script {
                    if (params.ENVIRONMENT == 'dev') {
                        echo "Dev URL: ${APP_URL}"
                    } else {
                        echo "Prod URL: ${APP_URL}"
                    }
                }
            }
        }
    }
}
