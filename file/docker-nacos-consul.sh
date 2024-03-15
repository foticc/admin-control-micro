docker run --restart=always --name=consul -d -p 8500:8500 -p 8600:8600/udp --name=badger hashicorp/consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0


docker run --restart=always --name=nacos --env PREFER_HOST_MODE=hostname --env MODE=standalone --env NACOS_AUTH_ENABLE=true -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.1.1
