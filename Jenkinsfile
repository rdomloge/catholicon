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
	    			// Start the container to run the integration tests against
	    			sh '''
	    				docker run --rm -d --name catholicon-integration-test -p 9090:8080 \
	    				rdomloge/catholicon:$BUILD_NUMBER
					'''
					
					sh "docker inspect -f '{{ .NetworkSettings.IPAddress }}' catholicon-integration-test"

					// This installs the standard wget - the one that ships with Jenkins BO doesn't have all the options available					
					sh 'apk add wget'
					// This makes the script wait until the container has warmed up
					waitUntil {
						sh '''
							wget --retry-connrefused --tries=10 --waitretry=5 -q \
							http://localhost:9090/seasons -O /dev/null
						'''
					}
					
					// This is just for some debug
	    			sh 'docker ps --format "{{.Ports}}" --filter="name=catholicon-integration-test"'
	    			
	    			// Run the integration tests against the running container
	    			sh 'mvn verify -Pfailsafe'
	    		}
			} 
			post{
				success{
				    echo "Integration tests passed"
				}

				always{
					script{
						// This is just here for debugging
						sh 'wget -O - http://localhost:9090/seasons'
						// Piping to true means the script returns true either way
						// We want to kill the container if it exists, and not fail if it didn't start 
					    sh 'docker kill catholicon-integration-test || true'
					}
				}
			}
    	}
		

    }
}
