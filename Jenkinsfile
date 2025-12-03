pipeline {
    agent any

    environment {
        BACKEND_IMAGE = "benyax38/toolrent-backend"
        FRONTEND_IMAGE = "benyax38/toolrent-frontend"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📥 Obteniendo código desde GitHub..."

                withCredentials([usernamePassword(
                    credentialsId: 'github-credentials',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {
                    sh """
                        git clone https://${GIT_USER}:${GIT_PASS}@github.com/benyax38/ToolRent-Tingeso.git repo
                    """
                }
            }
        }

        stage('Backend - Tests') {
            steps {
                echo "🧪 Ejecutando pruebas unitarias del backend..."
                dir("repo/Backend") {
                    sh "chmod +x mvnw"
                    sh "./mvnw test"
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                echo "🐳 Construyendo imagen Docker del backend..."
                sh "docker build -t ${BACKEND_IMAGE}:latest repo/Backend/"
            }
        }

        stage('Build Frontend Image') {
            steps {
                echo "🎨 Construyendo imagen Docker del frontend..."
                sh "docker build -t ${FRONTEND_IMAGE}:latest repo/Frontend/"
            }
        }

        stage('Docker Login & Push') {
            steps {
                echo "🔐 Autenticando y empujando imágenes a DockerHub..."

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                        docker push ${BACKEND_IMAGE}:latest
                        docker push ${FRONTEND_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline completado con éxito. Puedes desplegar usando Docker Compose."
        }
        failure {
            echo "❌ El pipeline falló. Revisa los logs."
        }
    }
}
