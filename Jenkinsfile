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
                sh 'mvn test'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
        
        stage('Building image') {
	    	steps{
	    		script {
		        	def workspace = env.WORKSPACE
        			echo "workspace directory is ${workspace}"
	        		def image = docker.build(registry + ":$BUILD_NUMBER")
            		//image.push 'master'
	        	}
	      	}
	    }
	    
	    stage('Publish') {
      		steps {
    			withDockerRegistry([ credentialsId: "ef879a02-b51a-49bb-a743-58f46dd8b4c8", url: "" ]) {
          			sh 'docker push rdomloge/catholicon'
        		}
      		}
    	}

    }
}
