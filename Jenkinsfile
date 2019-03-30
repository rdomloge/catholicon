pipeline {
    agent any
    
    environment {
		registry = "rdomloge/catholicon"
		registryCredential = 'rdomloge'
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
                sh 'mvn -Dmaven.test.ignore=true package'
            }
            post {
                success {
                	sh "chown jenkins:jenkins ${workspace}/target/catholicon.war"
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
        
        stage('Building image') {
        	
	    	steps{
	    		script {
		        	def workspace = env.WORKSPACE
        			echo "workspace directory is ${workspace}"
	        		docker.build(registry + ":$BUILD_NUMBER", "--build-arg ws=${workspace} .")
	        	}
	      	}
	    }
    }
}
