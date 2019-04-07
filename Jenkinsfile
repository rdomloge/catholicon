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
	    				docker run --rm -d --network="cicd" --name catholicon-integration-test -p 9090:8080 \
	    				rdomloge/catholicon:$BUILD_NUMBER
					'''
					
					CONTAINER_IP = sh(script: "docker container inspect -f '{{ .NetworkSettings.Networks.cicd.IPAddress }}' catholicon-integration-test", returnStdout:	true).trim()
					echo "Container is running on ${CONTAINER_IP}"

					// This installs the standard wget - the one that ships with Jenkins BO doesn't have all the options available					
					sh 'apk add wget'
					// This makes the script wait until the container has warmed up
					timeout(30) {
						waitUntil {
							sleep 4
							def r = sh script: "wget -q http://${CONTAINER_IP}:8080/seasons -O /dev/null", returnStatus: true
							return r == 0
						}
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
		
		stage('Release') {
			when{
			    branch "master"
			}

		    steps {
		        script {
		            sh "mvn -Dmaven.test.skip=true release:clean release:prepare release:perform -B"
		        }

		    }
		    post {
				failure {
			    	script {
						sh 'cat ~/.ssh/id_rsa.pub'
			    	}
		        }
    		}
		}
		
		stage('Deploy-prod') {
			when{
			    branch "master"
			}
			
		    steps {
		        script {
		        	echo "Deploying PROD build tagged '$BUILD_NUMBER'"
		            sh 'docker kill catholicon || true'
		            sh "docker run -d --name catholicon -p 8080:8080 localhost:5000/rdomloge/catholicon:$BUILD_NUMBER"
		        }
		    }
		}
		
		stage('Deploy-pre-prod') {
			when{
			    branch "develop"
			}
			
		    steps {
		        script {
		        	echo "Deploying PRE-PROD build tagged '$BUILD_NUMBER'"
		        	try {
			            sh 'docker kill catholicon-pre-prod'
		        	} catch(err) {
		        		echo 'Failed to stop pre-prod'      
		        	}
		        	
		        	try {
		        	    sh 'docker rm catholicon-pre-prod'
		        	} catch(err) {
						echo 'Failed to remove pre-prod'
		        	}
		        	
		            sh "docker run -d --name catholicon-pre-prod -p 8090:8090 localhost:5000/rdomloge/catholicon:$BUILD_NUMBER"
		        }
		    }
		}
		
		stage('Deploy-feature') {
		    when{
		        not {
		        	anyOf{
			            branch "develop"
			            branch "master"
		        	}
		        }
		    }
		    
		    steps{
			    script {
			        echo 'In a feature branch - deploying on a random port'
			        sh 'docker kill catholicon-feature || true'
			        sh "docker run -d --name catholicon-feature -P localhost:5000/rdomloge/catholicon:$BUILD_NUMBER"
			    }
		    }
		}

	}
}
