pipeline {

    agent any

    tools {
        jdk 'jdk11'
        maven 'maven3'
    }

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

        // Horizontal scaling / parallel: use a shared grid reachable by all agents (e.g. http://grid-host:4444)
        string(
            name: 'GRID_URL_PARAM',
            defaultValue: '',
            description: 'Optional. Grid URL for remote mode (e.g. http://selenium-grid:4444). Leave empty for default (localhost when grid runs on this agent). Required for multi-agent or shared grid.'
        )

        booleanParam(
            name: 'USE_SHARED_GRID',
            defaultValue: false,
            description: 'Use an already-running shared grid. When true, do NOT start/stop grid on this agent; GRID_URL_PARAM (or global GRID_URL) must point to the shared grid. Use for horizontal scaling / parallel runs.'
        )
    }

    environment {

        MAVEN_OPTS = '-Dmaven.repo.local=$WORKSPACE/.m2'

        GRID_COMPOSE = 'infrastructure/docker/docker-compose.grid-only.yml'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                script {
                    def paramUrl = (params.GRID_URL_PARAM ?: '').trim()
                    env.GRID_URL = paramUrl ?: env.GRID_URL ?: 'http://localhost:4444'
                }
            }
        }

        stage('Verify Environment') {
            steps {
                sh '''
                echo "Java version:"
                java -version

                echo "Maven version:"
                mvn -version
                '''
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
                expression { params.RUN_MODE == 'remote' && !params.USE_SHARED_GRID }
            }

            steps {

                sh """
                echo "Stopping previous grid (if any)"
                docker compose -f ${env.GRID_COMPOSE} down || true
                """

                sh """
                echo "Starting Selenium Grid on this agent..."
                docker compose -f ${env.GRID_COMPOSE} up -d \
                --scale chrome=3 \
                --scale firefox=0
                """
            }
        }

        stage('Wait for Grid') {

            when {
                expression { params.RUN_MODE == 'remote' }
            }

            steps {
                sh """
                    BASE=\${GRID_URL:-http://localhost:4444}
                    STATUS_URL="\${BASE%/}/status"
                    echo "Waiting for Selenium Grid at \$STATUS_URL (must be reachable from this agent)..."
                    echo "Tip: If Jenkins runs in Docker, localhost won't reach the host grid. Set GRID_URL_PARAM or global GRID_URL to http://host.docker.internal:4444 (Win/Mac) or http://<host-ip>:4444 (Linux)."
                    for i in \$(seq 1 30); do
                        BODY=\$(curl -s "\$STATUS_URL" 2>/dev/null || true)
                        if echo "\$BODY" | grep -q '"ready":true'; then
                            echo "Selenium Grid is ready"
                            exit 0
                        fi
                        if [ "\$i" = "1" ] && [ -z "\$BODY" ]; then
                            echo "curl returned empty (grid not reachable from this agent). If Jenkins is in Docker, set GRID_URL to http://host.docker.internal:4444 or host IP."
                        fi
                        echo "Grid not ready (\$i/30)..."
                        sleep 2
                    done
                    echo "Grid failed to start or unreachable. If Jenkins runs in Docker, set GRID_URL_PARAM (or Jenkins global GRID_URL) to http://host.docker.internal:4444 or http://<host-ip>:4444"
                    exit 1
                """
            }
        }

        stage('Run Tests') {

            steps {

                script {

                    if (params.SCOPE == 'ui') {

                        sh """
                        mvn -pl web-ui test -B \
                        -Drun.mode=${params.RUN_MODE} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS} \
                        -Dgrid.url=${env.GRID_URL}
                        """

                    } else if (params.SCOPE == 'api') {

                        sh "mvn -pl api test -B"

                    } else {

                        sh """
                        mvn test -B \
                        -Drun.mode=${params.RUN_MODE} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS} \
                        -Dgrid.url=${env.GRID_URL}
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

                if (params.RUN_MODE == 'remote' && !params.USE_SHARED_GRID) {

                    sh """
                    echo "Stopping Selenium Grid on this agent..."
                    docker compose -f ${env.GRID_COMPOSE} down || true
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