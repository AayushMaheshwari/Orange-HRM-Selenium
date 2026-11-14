pipeline {
    agent any

    tools {
        maven 'maven-3.9.16'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/AayushMaheshwari/Orange-HRM-Selenium.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/java/resources/ExtendReport',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'HTML Spark Report'
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/src/test/java/resources/ExtendReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }
        success {
            emailext (
                to: 'aayushmaheshwarigi111@gmail.com',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>

                <p>The latest Jenkins build has completed.</p>

                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Last Commit:</b></p>
                <p>${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>

                <p><b>Build log is attached.</b></p>

                <p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/"> Click Here</a></p>

                <p>Best Regards,</p>
                <p><b>Automation Team</b></p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }

        failure {
            emailext (
                to: 'aayushmaheshwarigi111@gmail.com',
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <html>
                <body>
                <p>Hello Team,</p>

                <p>The latest Jenkins build has <b style="color:red;">FAILED</b>.</p>

                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: red;"><b>FAILED &#10060;</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Last Commit:</b></p>
                <p>${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>

                <p><b>Build log is attached.</b></p>
                <p><b>Please check the logs and take necessary actions.</b></p>

                <p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/"> Click Here</a></p>

                <p>Best Regards,</p>
                <p><b>Automation Team</b></p>
                </body>
                </html>
                """,
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}