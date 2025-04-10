pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'prod'], description: 'Select the environment to deploy to ')
        string(name: 'VERSION', defaultValue: '1.0', description: 'Enter the version tag for the Docker image (e.g., 1.2)')
    }
    
    environment {
        IMAGE_NAME = "java-calculator-app"
        IMAGE_TAG = "${params.VERSION}"
        PORT = "${params.ENVIRONMENT == 'dev' ? '9090' : '9091'}"
        SONARQUBE_URL = "https://sonarcloud.io"
        ARTIFACTORY_URL = "http://localhost:8082"
        SONAR_TOKEN = credentials('sonar-token')
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    if (params.ENVIRONMENT == 'dev') {
                        git branch: 'dev', url: 'https://github.com/kamaldinesh/bench_devops_assignments-dev.git'
                    } else {
                        git branch: 'prod', url: 'https://github.com/kamaldinesh/bench_devops_assignments-dev.git'
                    }
                }
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
                      sh "mvn sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=${SONAR_TOKEN} -Dsonar.organization=kamalkantnimawat -Dsonar.projectKey=CalculatorMvcProject -Dsonar.jacoco.reportPaths=target/site/jacoco/jacoco.xml"
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
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image
                    sh "docker build -t ${IMAGE_NAME}-${params.ENVIRONMENT}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                script {
                    def containerId = powershell(script: "docker ps -q --filter name=${IMAGE_NAME}-${params.ENVIRONMENT}", returnStdout: true).trim()
                    if (containerId) {
                        sh "docker rm -f ${containerId}"
                    }
                    sh "docker run -d -p ${PORT}:${PORT} --name ${IMAGE_NAME}-${params.ENVIRONMENT} ${IMAGE_NAME}-${params.ENVIRONMENT}:${IMAGE_TAG}"
                }
            }
        }

    }
    
    post {
        success {
            echo "Build and Deployment Successful"
            script {
                try {
                    emailext(
                        to: 'kamalkantnimawat28@gmail.com',
                        subject: "Build Successful - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                        body: """
                        <html>
                        <body style="font-family: Arial, sans-serif; background-color: #e6f7e1; color: #333333; padding: 20px;">
                            <h2 style="color: #4CAF50; text-align: center;">Build Successful</h2>
                            <p style="font-size: 16px;">The build <strong>${env.JOB_NAME} - ${env.BUILD_NUMBER}</strong> was successful.</p>
                            <p style="font-size: 16px;">You can view the results and the details of this build at the following link:</p>
                            <p style="font-size: 16px;">
                                <a href="${env.BUILD_URL}" style="color: #4CAF50; text-decoration: none;">View Build</a>
                            </p>
                        </body>
                        </html>
                        """,
                        mimeType: 'text/html'
                    )
                } catch (Exception e) {
                    echo "Failed to send email: ${e.message}"
                }
            }
        }
        
        failure {
            echo "Build or Deployment Failed"
            script {
                try {
                    emailext(
                        to: 'kamalkantnimawat28@gmail.com',
                        subject: "Build Failed - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                        body: """
                        <html>
                        <body style="font-family: Arial, sans-serif; background-color: #f8d7da; color: #721c24; padding: 20px;">
                            <h2 style="color: #721c24; text-align: center;">Build Failed</h2>
                            <p style="font-size: 16px;">The build <strong>${env.JOB_NAME} - ${env.BUILD_NUMBER}</strong> has failed.</p>
                            <p style="font-size: 16px;">Unfortunately, the build did not complete successfully. Please review the details and logs:</p>
                            <p style="font-size: 16px;">
                                <a href="${env.BUILD_URL}" style="color: #721c24; text-decoration: none;">View Build Logs</a>
                            </p>
                        </body>
                        </html>
                        """,
                        mimeType: 'text/html'
                    )
                } catch (Exception e) {
                    echo "Failed to send email: ${e.message}"
                }
            }
        }
    }
}
