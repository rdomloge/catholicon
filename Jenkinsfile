pipeline {
    agent any
    
    environment {
		registry = "docker_hub_account/repository_name"
		registryCredential = 'dockerhub'
	}

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                    node {
	                    checkout scm
	    				def customImage = docker.build("my-image:${env.BUILD_ID}")
	    				customImage.push() 
                    }

                }
            }
        }
        
        stage('Building image') {
	    	steps{
	    		script {
	        		docker.build registry + ":$BUILD_NUMBER"
	        	}
	      	}
	    }
    }
}