pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK11'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=true"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test (Docker)') {
            steps {
                script {
                    sh '''
                    docker build -t testng-framework .
                    docker run --name testng-run testng-framework || true
                    docker cp testng-run:/app/target ./target
                    docker rm testng-run
                    '''
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML([
                    reportDir: 'target',
                    reportFiles: 'extent-report.html',
                    reportName: 'Extent Test Report'
                ])
            }
        }

        stage('Publish Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.png', fingerprint: true
        }
    }
}
