pipeline {
    agent any
    triggers {
        pollSCM 'https://github.com/viniciusfernandes/ovigia-api.git'
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew assemble'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
    }
}