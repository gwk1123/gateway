
#!/bin/sh
web_path=/jar/gateway #定义 tomcat 的位置
startup_path=/jenkins/workspace/gateway/docker/startup.sh
shutdown_path=/jenkins/workspace/gateway/docker/shutdown.sh
docker_path=/jenkins/workspace/gateway/docker/Dockerfile
project_web=/jenkins/workspace/gateway/target/gateway-0.0.1-SNAPSHOT.jar #定义项目打包位置
if [ -f $project_web ] ;then
echo -- stop tomcat
#kill tomcat pid
pidlist=`ps -ef | grep gateway | grep -v grep | awk '{print $2}'` #查看是否有进程正在运行
if [ "$pidlist" = "" ] #没有进程在运行,则提示
then
echo "no gateway pid alive!"
else #有进程在运行,杀掉
echo "gateway Id list :$pidlist"
kill -9 $pidlist
echo "KILL $pidlist:"
echo "gateway stop success"
fi
echo -- 正在部署$project_web #将进程杀掉之后,重新进行部署
if [ -f $web_path/gateway-0.0.1-SNAPSHOT.jar ] ;then #查看 /jar 目录下是否有以前的 jar 包,如果有,删掉
rm -f $web_path/gateway-0.0.1-SNAPSHOT.jar
echo -- 正在删除$project_web
fi

rm -rf $web_path

cd /
mkdir $web_path
echo -- 正在1
cp $project_web $web_path #将新生成的 war 包复制到 /webapps 目录下
echo -- 正在2
cp $startup_path $web_path
echo -- 正在3
cp $shutdown_path $web_path
echo -- 正在4
mv -f $docker_path $web_path
echo -- 正在5
# start tomcat
cd $web_path
chmod u+x *.sh
./shutdown.sh
./startup.sh
else
echo $BUILD_ID - 未找到$project_web
fi






