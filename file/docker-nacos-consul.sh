docker run --restart=always --name=consul -d -p 8500:8500 -p 8600:8600/udp --name=badger hashicorp/consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0


docker run --restart=always --name=nacos --env PREFER_HOST_MODE=hostname --env MODE=standalone --env NACOS_AUTH_ENABLE=true -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.1.1


docker run -d -p 8080:9000 --restart=always --name=portainer -v /var/run/docker.sock:/var/run/docker.sock --name prtainer  portainer/portainer

curl -o redis.conf http://download.redis.io/redis-stable/redis.conf
docker run --restart=always --log-opt max-size=100m --log-opt max-file=2 -p 6379:6379 --name redis -v /home/linux/Documents/redis/redis.conf:/etc/redis/redis.conf -v /home/linux/Documents/redis/data:/data -d redis redis-server /etc/redis/redis.conf  --appendonly yes  --requirepass 123456