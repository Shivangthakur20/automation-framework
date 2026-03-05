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

        choice(name: 'SCOPE',
               choices: ['ui', 'api', 'all'],
               description: 'What to run: UI, API, or all modules')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
        GRID_COMPOSE = 'infrastructure/docker/docker-compose.grid-only.yml'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests -q'
            }
        }

        stage('Start Grid') {
            when {
                expression { params.RUN_MODE == 'remote' }
            }
            steps {
                sh "docker compose -f ${env.GRID_COMPOSE} up -d --scale chrome=3 --scale firefox=0"
                script {
                    def gridReady = false
                    for (int i = 0; i < 30; i++) {
                        def status = sh(script: "curl -s http://selenium-hub:4444/status 2>/dev/null || true", returnStdout: true).trim()
                        if (status.contains('"ready":true')) {
                            gridReady = true
                            break
                        }
                        sleep(time: 2, unit: 'SECONDS')
                    }
                    if (!gridReady) {
                        error('Selenium Grid did not become ready in time')
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    if (params.SCOPE == 'ui') {
                        sh """
                            mvn -pl web-ui test -q \
                            -Drun.mode=${params.RUN_MODE} \
                            -Dbrowser=${params.BROWSER} \
                            -Dheadless=${params.HEADLESS}
                        """
                    } else if (params.SCOPE == 'api') {
                        sh 'mvn -pl api test -q'
                    } else {
                        sh """
                            mvn test -q \
                            -Drun.mode=${params.RUN_MODE} \
                            -Dbrowser=${params.BROWSER} \
                            -Dheadless=${params.HEADLESS}
                        """
                    }
                }
            }
        }

        stage('Generate Allure Report') {
            when {
                anyOf {
                    expression { params.SCOPE == 'ui' }
                    expression { params.SCOPE == 'api' }
                    expression { params.SCOPE == 'all' }
                }
            }
            steps {
                script {
                    if (params.SCOPE == 'ui') {
                        sh 'mvn -pl web-ui allure:report -q'
                    } else if (params.SCOPE == 'api') {
                        sh 'mvn -pl api allure:report -q'
                    } else {
                        sh 'mvn -pl web-ui allure:report -q'
                        sh 'mvn -pl api allure:report -q'
                    }
                }
            }
        }
    }

    post {

        always {
            archiveArtifacts artifacts: '**/target/**', fingerprint: true, allowEmptyArchive: true

            script {
                if (params.RUN_MODE == 'remote') {
                    sh "docker compose -f ${env.GRID_COMPOSE} down --remove-orphans 2>/dev/null || true"
                }
            }
        }

        failure {
            echo 'Build failed.'
        }

        success {
            echo 'Build successful.'
        }
    }
}