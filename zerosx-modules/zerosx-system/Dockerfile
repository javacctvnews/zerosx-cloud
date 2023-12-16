# 基础镜像
FROM  openjdk:8-jre
# author
MAINTAINER javacctvnews

# 挂载目录
VOLUME /home/zerosx-cloud
# 创建目录
RUN mkdir -p /home/zerosx-cloud
# 指定路径
WORKDIR /home/zerosx-cloud
# 复制jar文件到路径
COPY ./target/zerosx-system.jar /home/zerosx-cloud/zerosx-system.jar
# 启动网关服务
ENTRYPOINT ["java",\
            "-jar",\
            "-Xms300m", "-Xmx300m",\
            "-Djava.security.egd=file:/dev/./urandom",\
            "-XX:-OmitStackTraceInFastThrow",\
            "zerosx-system.jar"]

#build镜像
#docker build -t zerosx-cloud/zerosx-system:0.0.1 .