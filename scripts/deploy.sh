#!/bin/bash
BUILD_JAR=$(ls /home/ubuntu/app/deploy/podor-*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/app/deploy/deploy.log

echo "> build 파일 복사" >> /home/ubuntu/app/deploy/deploy.log
DEPLOY_PATH=/home/ubuntu/app/deploy/
cp $BUILD_JAR $DEPLOY_PATH

IDLE_APPLICATION=$IDLE_PROFILE-$JAR_NAME
IDLE_APPLICATION_PATH=$DEPLOY_PATH$IDLE_APPLICATION

ln -Tfs $DEPLOY_PATH$JAR_NAME $IDLE_APPLICATION_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/app/deploy/deploy.log

IDLE_PID=$(pgrep -f $IDLE_APPLICATION)

if [ -z $IDLE_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/app/deploy/deploy.log
fi

echo "> $IDLE_PROFILE 배포"    >> /home/ubuntu/app/deploy/deploy.log
nohup java -jar -Duser.timezone=GMT+9 -Dspring.profiles.active=$IDLE_PROFILE $IDLE_APPLICATION_PATH >> /home/ubuntu/app/deploy/deploy.log 2>/home/ubuntu/app/deploy/deploy_err.log &  
  
