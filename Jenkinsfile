pipeline {
    agent any
    tools{
        maven 'maven_3_8_1'
    }
    stages{
        stage('Build maven'){
            steps{
                dir('backend'){
                    sh 'mvn clean package'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t benyax38/toolrent-repo:latest .'
                }
            }
        }

        stage('Push image to Docker Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                        sh 'docker login -u benyax38 -p %dhpsw%'
                    }
                    sh 'docker push benyax38/toolrent-repo:latest'
                }
            }
        }
    }
}