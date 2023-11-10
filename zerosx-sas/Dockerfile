# 基础镜像
FROM  alibabadragonwell/dragonwell:17-anolis
# author
MAINTAINER javacctvnews

# 挂载目录
VOLUME /home/zerosx-cloud
# 创建目录
RUN mkdir -p /home/zerosx-cloud
# 指定路径
WORKDIR /home/zerosx-cloud
# 复制jar文件到路径
COPY ./target/zerosx-sas.jar /home/zerosx-cloud/zerosx-sas.jar
# 启动网关服务
ENTRYPOINT ["java",\
            "-jar",\
            "--add-opens=java.base/java.lang=ALL-UNNAMED",\
            "--add-opens=java.base/sun.net=ALL-UNNAMED",\
            "--add-exports=java.base/sun.net.util=ALL-UNNAMED",\
            "-Xms300m", "-Xmx300m",\
            "-Djava.security.egd=file:/dev/./urandom",\
            "-XX:-OmitStackTraceInFastThrow",\
            "zerosx-sas.jar"]

#build镜像
#docker build -t zerosx-cloud/zerosx-sas:0.0.1 .