pipeline {
    agent any
    tools{
        maven 'maven_3_8_1'
    }
    stages{
        stage('Build maven'){
            steps{
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t benyax38/toolrent-repo:latest .'
                }
            }
        }

        stage('Push image to Docker Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                        bat 'docker login -u benyax38 -p %dhpsw%'
                    }
                    bat 'docker push benyax38/toolrent-repo:latest'
                }
            }
        }
    }
}