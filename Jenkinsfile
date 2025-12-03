pipeline {
    agent any

    environment {
        // Credenciales configuradas en Jenkins
        GITHUB_USER = credentials('github-user')
        GITHUB_PASS = credentials('github-token')
        DOCKER_USER = credentials('dockerhub-user')
        DOCKER_PASS = credentials('dockerhub-pass')

        // Nombres de imágenes en DockerHub
        BACKEND_IMAGE = "benyax38/toolrent-backend"
        FRONTEND_IMAGE = "benyax38/toolrent-frontend"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📥 Obteniendo código desde GitHub..."
                git url: "https://${GITHUB_USER}:${GITHUB_PASS}@github.com/benyax38/ToolRent-Tingeso.git", branch: 'main'
            }
        }

        stage('Backend - Tests') {
            steps {
                echo "🧪 Ejecutando pruebas unitarias del backend..."
                dir("backend") {
                    sh "./mvnw test"
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                echo "🐳 Construyendo imagen Docker del backend..."
                sh """
                    docker build -t ${BACKEND_IMAGE}:latest backend/
                """
            }
        }

        stage('Build Frontend Image') {
            steps {
                echo "🎨 Construyendo imagen Docker del frontend..."
                sh """
                    docker build -t ${FRONTEND_IMAGE}:latest frontend/
                """
            }
        }

        stage('Login DockerHub') {
            steps {
                echo "🔐 Iniciando sesión en DockerHub..."
                sh """
                    echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                """
            }
        }

        stage('Push Images to DockerHub') {
            steps {
                echo "⛵ Subiendo imágenes al DockerHub..."
                sh """
                    docker push ${BACKEND_IMAGE}:latest
                    docker push ${FRONTEND_IMAGE}:latest
                """
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
