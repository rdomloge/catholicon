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
                sh 'mvn -Dmaven.test.skip=true package'
            }
        }
        
        stage('Unit tests') {
            steps {
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
    	
    	stage('Integration tests') {
	    	steps{
	    		script{
	    			sh '''
	    				docker run --rm -d --name catholicon-integration-test -P rdomloge/catholicon:$BUILD_NUMBER
	    				docker ps --format "{{.Ports}}" --filter="name=catholicon-integration-test"
					'''
	    		}
			} 
			post{
				success{
				    sh 'mvn verify -Pfailsafe'
				}

				always{
					script{
						// piping to true means the script returns true either way
					    sh 'docker kill catholicon-integration-test || true'
					}
				}
			}
    	}
		

    }
}
