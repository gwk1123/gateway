APPNAME=gateway
PORT=8091
docker build -t $APPNAME .
docker run -d --name $APPNAME -p $PORT:$PORT $APPNAME
