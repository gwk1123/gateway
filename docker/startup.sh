APPNAME=gateway
PORT=8091
docker build -t $APPNAME .
docker run -d --name $APPNAME --net=host -p $PORT:$PORT $APPNAME
