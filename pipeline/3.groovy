pipeline{
    agent any
    stages{
        stage('pull'){
            steps{
              git poll: false, url: 'https://github.com/jredel/jenkins-helloworld.git'
            }
        }
        stage('build'){
            steps {
                // On génère le dockerfile à la volé pour le test, il faudrait qu'il soit dans le dépôt
              sh '''
                echo 'FROM eclipse-temurin:17-jdk
                COPY Main.java /app/Main.java
                WORKDIR /app
                RUN javac Main.java
                CMD ["java", "Main"]' > Dockerfile
                echo '---'
                cat Dockerfile
                echo '---'
                docker build -t helloworld .
            '''
            }
        }

        stage('tag'){
            steps {
                sh '''
                  docker tag helloworld registry:5000/helloworld
                '''
            }
        }

        stage('push'){
            steps {
                sh '''
                  docker push helloworld
                '''
            }
        }

        stage('run'){
            steps {
                sh 'docker run helloworld'
            }
        }
    }
}