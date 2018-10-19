# app-server
app 版本管控台

要求
    jdk1.8+

生成证书
    keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
        
启动
    java -jar name.jar --server.port=端口 --spring.profiles.active=test