pipeline {

    agent any

    parameters {

        choice(
            name: 'RUN_MODE',
            choices: ['local', 'remote'],
            description: 'Execution Mode'
        )

        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests in headless mode'
        )

        choice(
            name: 'SCOPE',
            choices: ['ui', 'api', 'all'],
            description: 'Which tests to run'
        )
    }

    environment {

        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'

        GRID_COMPOSE = 'infrastructure/docker/grid/docker-compose.yml'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {

            steps {

                sh '''
                mvn -B clean install \
                -DskipTests \
                -Ddependency-check.skip=true
                '''
            }
        }

        stage('Start Selenium Grid') {

            when {
                expression { params.RUN_MODE == 'remote' }
            }

            steps {

                sh """
                docker-compose -f ${env.GRID_COMPOSE} up -d \
                --scale chrome=3 \
                --scale firefox=0
                """

                sh '''
                echo "Waiting for Selenium Grid..."

                for i in {1..30}
                do
                    STATUS=$(curl -s http://localhost:4444/status || true)

                    if echo "$STATUS" | grep -q '"ready":true'
                    then
                        echo "Selenium Grid is ready"
                        exit 0
                    fi

                    sleep 2
                done

                echo "Grid failed to start"
                exit 1
                '''
            }
        }

        stage('Run Tests') {

            steps {

                script {

                    if (params.SCOPE == 'ui') {

                        sh """
                        mvn -pl web-ui test \
                        -Drun.mode=${params.RUN_MODE} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS}
                        """

                    }

                    else if (params.SCOPE == 'api') {

                        sh "mvn -pl api test"

                    }

                    else {

                        sh """
                        mvn test \
                        -Drun.mode=${params.RUN_MODE} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS}
                        """

                    }
                }
            }
        }

        stage('Generate Allure Report') {

            steps {

                sh 'mvn -pl web-ui allure:report || true'
                sh 'mvn -pl api allure:report || true'

            }
        }

    }

    post {

        always {

            archiveArtifacts artifacts: '**/target/**', fingerprint: true

            script {

                if (params.RUN_MODE == 'remote') {

                    sh """
                    docker-compose -f ${env.GRID_COMPOSE} down
                    """

                }

            }

        }

        success {
            echo 'Build successful'
        }

        failure {
            echo 'Build failed'
        }

    }

}