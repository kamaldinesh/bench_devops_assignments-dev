pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'prod'], description: 'Select the environment to deploy to')
        string(name: 'VERSION', defaultValue: '1.0', description: 'Enter the version tag for the Docker image (e.g., 1.2)')
    }
    
    environment {
        IMAGE_NAME = "java-calculator-app"
        IMAGE_TAG = "${params.VERSION}"
        PORT = "${params.ENVIRONMENT == 'dev' ? '9090' : '9091'}"
        SONARQUBE_URL = "https://sonarcloud.io/"
        ARTIFACTORY_URL = "http://localhost:8082"
    }
    
    stages {
        stage('Checkout') {
              steps {
                script {
                    if (params.ENVIRONMENT == 'dev') {
                        git branch: 'dev', url: 'https://github.com/kamaldinesh/bench_devops_assignments-dev.git'
                    }
                    else
                    {
                         git branch: 'prod', url: 'https://github.com/kamaldinesh/bench_devops_assignments-dev.git'
                    }
                }
              }
        }
        
        stage('Build-Maven') {
            steps {
                bat 'mvn clean package'
            }
        }
        
        stage('Unit-Test') {
            steps {
                bat 'mvn test'
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
bat "mvn sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=c2275d524506c46bd1bc94c76e3674cc402d7668 -Dsonar.organization=kamalkantnimawat -Dsonar.projectKey=CalculatorMvcProject -Dsonar.jacoco.reportPaths=target/site/jacoco/jacoco.xml"
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
                                "target": "java-calculator-app-artifactory/binaries/${params.ENVIRONMENT}/"
                            }
                        ]
                    }"""
                    rtUpload serverId: 'Artifactory', spec: uploadSpec
                    rtPublishBuildInfo serverId: 'Artifactory'
                }
            }
        }
        
        // stage('Build-Docker') {
        //     steps {
        //         withCredentials([usernamePassword(credentialsId: 'Artifactory-Auth', usernameVariable: 'ARTIFACTORY_USERNAME', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
        //             sh "docker build --build-arg ARTIFACTORY_USERNAME=\${ARTIFACTORY_USERNAME} --build-arg ARTIFACTORY_PASSWORD=\${ARTIFACTORY_PASSWORD} --build-arg ARTIFACTORY_URL=${ARTIFACTORY_URL}/artifactory --build-arg ENVIRONMENT=${params.ENVIRONMENT} -t ${IMAGE_NAME}:${IMAGE_TAG} ."
        //         }
        //     }
        // }
        
        // stage('Docker-Push') {
        //     steps {
        //         script {
        //             docker.withRegistry('https://index.docker.io/v1/', 'docker-login') {
        //                 docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
        //             }
        //         }
        //     }
        // }
        
        // stage('Run-container') {
        //     steps {
        //         script {
        //             sh "docker stop endpointapi-${params.ENVIRONMENT} || true"
        //             sh "docker rm endpointapi-${params.ENVIRONMENT} || true"
        //             sh "docker run -d --name endpointapi-${params.ENVIRONMENT} -p ${PORT}:8080 ${IMAGE_NAME}:${IMAGE_TAG}"
        //         }
        //     }
        // }
        
         stage('Build Docker Image') {
            steps {
                script {
                    
                    // Build Docker image
                    bat "docker build -t ${IMAGE_NAME}-${params.ENVIRONMENT}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                script {
                    def containerId = powershell(script: "docker ps -q --filter name=${IMAGE_NAME}-${params.ENVIRONMENT}", returnStdout: true).trim()
                    if (containerId) {
                        bat "docker rm -f ${containerId}"
                    }

                    // def port = (params.Environment == 'dev') ? '9090' : '9091'
                    bat "docker run -d -p ${PORT}:${PORT} --name ${IMAGE_NAME}-${params.ENVIRONMENT} ${IMAGE_NAME}-${params.ENVIRONMENT}:${IMAGE_TAG}"
                }
            }
        }

    }
}
