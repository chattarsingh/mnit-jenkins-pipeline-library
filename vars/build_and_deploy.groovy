
def call(Map pipelineParams) {
  try{
    timeout(time: 60, unit: 'MINUTES') {
      pipeline {
        env.REPO = pipelineParams.REPO
        env.APP_NAME = pipelineParams.APP_NAME
        env.MVN_HOME = pipelineParams.MVN_HOME
        node {
          stage("Code Checkout") {
            sh '''
              rm -rf ${WORKSPACE}/*
              git clone ${REPO}
            '''
          }
          stage("Build") {
            sh '''
               cd ${APP_NAME}
              ${MVN_HOME}/mvn clean install
            '''
          }
          stage("Static Code Analysis") {
            sh '''
              echo "Static Code Analysis"
            '''
          }
          stage("Delete Previous Deployment") {
            sh '''
              echo "Delete Previous Deployment"
            '''
          }  
          stage("Deploy") {
            sh '''
              echo "Deployment"
            '''
          }
        }
        if(pipelineParams.EMAIL_TO_LIST?.trim()){   
          echo "email send enabled"   
          //new sendEmail().call(pipelineParams,"SUCCESS")   
        } 

      }
    }
  }catch (err) {
    echo "in catch block" 
    echo "Caught: ${err}" 
    currentBuild.result = 'FAILURE' 
    if(pipelineParams.EMAIL_TO_LIST?.trim()){   
      echo "email send enabled"   
      //sendEmail().call(pipelineParams,"FAILURE")   
    }  
    throw err
  }
}
