def CONTAINER_IP
def IMAGE

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
	        		IMAGE = docker.build(registry + ":$BUILD_NUMBER")
	        	}
	      	}
	    }
	    
	    stage('Publish') {
      		steps {
      			// To push to Docker hub
    			//withDockerRegistry([ credentialsId: "ef879a02-b51a-49bb-a743-58f46dd8b4c8", url: "" ]) {
          		//	sh 'docker push rdomloge/catholicon'
        		//}
        		
        		// To push to local registry
        		script{
	        		docker.withRegistry('https://localhost:5000') {
				        //sh 'docker push rdomloge/catholicon'
				        IMAGE.push();
				    }
			    }
      		}
    	}
    	
    	stage('Start container'){
    	    steps{
    			script{
	    			// Start the container to run the integration tests against
	    			sh '''
	    				docker run --rm -d --name catholicon-integration-test -p 9090:8080 \
	    				rdomloge/catholicon:$BUILD_NUMBER
					'''
					
					CONTAINER_IP = sh(script: "docker container inspect -f '{{ .NetworkSettings.IPAddress }}' catholicon-integration-test", returnStdout:	true).trim()
					echo "Container is running on ${CONTAINER_IP}"

					// This installs the standard wget - the one that ships with Jenkins BO doesn't have all the options available					
					sh 'apk add wget'
					// This makes the script wait until the container has warmed up
					waitUntil {
						return sh("wget --retry-connrefused --tries=20 --waitretry=5 -q \
							'http://${CONTAINER_IP}:8080/seasons' -O /dev/null", returnStatus: true) == 0
					}
					//sleep 30
				}
			}
			post {
				failure {
				    script{
						// Piping to true means the script returns true either way
						// Need to kill the container if we failed to start it, for the next run 
					    sh 'docker kill catholicon-integration-test || true'
					}
				}


			}
    	}

    	
    	stage('Integration tests') {
    		steps{
    			script{
	    			// Run the integration tests against the running container
	    			sh "mvn verify -Pfailsafe -Dip=${CONTAINER_IP}"
    			}
    		}
			post{
				success{
				    echo "Integration tests passed"
				}

				always{
					script{
						// Piping to true means the script returns true either way
						// We want to kill the container if it exists, and not fail if it didn't start 
					    sh 'docker kill catholicon-integration-test || true'
					}
				}
			}
    	}
		

    }
}
