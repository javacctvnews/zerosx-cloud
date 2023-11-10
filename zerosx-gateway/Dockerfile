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
COPY ./target/zerosx-gateway.jar /home/zerosx-cloud/zerosx-gateway.jar
# 启动网关服务
ENTRYPOINT ["java", \
            "-jar", \
            "-Xms300m", "-Xmx300m", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-XX:-OmitStackTraceInFastThrow", \
            "zerosx-gateway.jar"]

#build镜像
#docker build -t zerosx-cloud/zerosx-gateway:0.0.3 .
