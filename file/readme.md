
使用api-auth作为认证中心，认证通过的access_token和refresh_token会缓存

api-auth 作为authorization-server

目前有两种方案

- ### gateway网关作为authorization-server的client和resource-server,它处理了所有请求的认证和鉴权，

    这样做会把压力都给到网关处,并且其他微服务模块的方法级别鉴权不太好做,这种方式鉴权只能由api级别的鉴权方式
    rbac  user---role---(permission--->module)---api,我们需要维护api,并且是每个模块单独api

- ### 每个微服务模块自己去做认证和鉴权
    
    扩展性差，重复性,需要把每个微服务作为一个resource-server注册,需要维护client


1. ### 关于认证的过程

    spring oauth2 默认的方式是client或resource-server配置认证中心的断点信息,通过http请求去认证中心
    请求验证token是否合法等(详情见`OpaqueTokenIntrospector`的实现类,他是token内省的接口)
    
    - 现在我们通过自己扩展实现认证信息的保存(`OAuth2AuthorizationService`),将认证信息缓存
    - 在token内省时去查询缓存即可优点是可以减少与认证中心的通信次数，降低网络开销，并且提高了性能
    


2. 关于认证的过程
   spring oauth2 默认的放





