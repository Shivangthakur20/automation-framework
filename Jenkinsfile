pipeline {

    agent any

    parameters {
        choice(name: 'RUN_MODE',
               choices: ['local', 'remote'],
               description: 'Execution Mode')

        choice(name: 'BROWSER',
               choices: ['chrome', 'firefox'],
               description: 'Browser')

        booleanParam(name: 'HEADLESS',
                     defaultValue: true,
                     description: 'Run in headless mode')

        string(name: 'SUITE',
               defaultValue: 'testng.xml',
               description: 'TestNG suite file')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Start Grid') {
            when {
                expression { params.RUN_MODE == 'remote' }
            }
            steps {
                sh 'docker compose up -d --scale chrome=3'
                sh 'sleep 10'
            }
        }

        stage('Run Tests') {
            steps {
                sh """
                    mvn clean test \
                    -Drun.mode=${params.RUN_MODE} \
                    -Dbrowser=${params.BROWSER} \
                    -Dheadless=${params.HEADLESS} \
                    -Dsuite=${params.SUITE}
                """
            }
        }

        stage('Generate Allure Report') {
            steps {
                sh 'mvn allure:report'
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'target/**', fingerprint: true

            script {
                if (params.RUN_MODE == 'remote') {
                    sh 'docker compose down'
                }
            }
        }

        failure {
            echo "Build failed."
        }

        success {
            echo "Build successful."
        }
    }
}