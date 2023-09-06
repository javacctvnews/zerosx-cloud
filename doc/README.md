<h2 style="text-align:center">组件安装部署操作说明</h2>

> <a href="https://docs.docker.com/compose/install/">Docker Desktop安装可参考官网：https://docs.docker.com/compose/install </a>

> 建议：初学者按文档介绍的组件顺序执行脚本进行安装，比较不容易出错，即Redis、MySql、Nacos、Seata的顺序

### 1. Redis
1. 版本：6.2.13，配置文件中的密码：zerosx@123456，端口：6479（可自行修改）
2. 执行安装：
    ```shell
    # 进入目录(可用拷贝到指定目录)
    cd doc/redis
    # 安装(指定文件)
    docker-compose -f docker-compose.yaml up -d
    ```

### 2. MySql8
1. 版本：8.0.34，端口：13306，密码：zerosx@123456

2. 执行安装：
   ```shell
   # 进入目录
   cd doc/mysql8
   # 安装(指定文件)
   docker-compose -f docker-compose.yaml up -d
   ```
### 3. Nacos
1. 版本：2.2.0 ，端口：8848

2. 执行nacos数据库脚本：
   ```shell
   执行SQL脚本，文件所在目录：doc/nacos/mysql-schema.sql
   ```
   
3. 修改文件：doc/nacos/docker-compose-nacos2.2.0.yaml，修改项如下：
   ```shell
   #把ip修改成安装主机的IP
   - MYSQL_SERVICE_HOST=192.168.3.6
   ```
   
4. 执行安装
   ```shell
   # 进入目录
   cd doc/nacos
   # 安装(指定文件)
   docker-compose -f docker-compose-nacos2.2.0.yaml up -d
   ```
   
5. 登录Nacos控制台：http://127.0.0.1:8848/nacos 用户名：nacos 密码：nacos
   * 新建命名空间，表单内容填写如下：
     
     > 命名空间ID: 32c6d87e-c726-44c5-a87b-a89b0f91c63d
     >
     > 命名空间名: local
     >
     > 描述: 本地开发使用
     >
     > ![Image text](./nacos/p1.png)
     
   * 导入微服务配置
     
     * 方式1：按【doc/zerosx/nacos-config】下文件名逐个新建配置，分组Group名：local
     
       ```shell
       # common-local.yaml  		#所有服务共享的配置
       # seataServer.properties	#seata组件的配置
       
       # zerosx-gateway-local.yaml	#zerosx-gateway网关配置
       # zerosx-auth-local.yaml	#zerosx-auth服务配置
       # zerosx-system-local.yaml	#zerosx-system服务配置
       
       ```
     
     * 方式2：导入方式，压缩文件【doc/zerosx/nacos-config/nacos_config_export_20230905.zip】
### 4. Seata

1. 版本：1.6.1

2. 创建Seata数据库表

   ```shell
   执行SQL脚本，文件所在目录：doc/seata/mysql-schema.sql
   ```

3. 修改【doc/seata/docker-compose-seata1.6.1.yaml】，修改内容：

   ```shell
   # seata安装主机的IP
   - SEATA_IP=192.168.3.6
   ```

   

4. 修改【doc/seata/seata-server】下的文件

   * 文件是从官方镜像【seataio/seata-server:1.6.1】中拷贝出来的文件（官方安装文档中有此说明）

   * 修改【doc/seata/seata-server/resources/application.yml】 ，修改内容如下（有2处）：

     ```shell
     # 修改成nacos的ip端口
     server-addr: 192.168.3.6:8848
     ```

     

5. 新增Nacos配置【seataServer.properties】，并修改如下内容：

   ```shell
   # Seata数据库
   store.db.url=jdbc:mysql://192.168.3.6:13306/seata_v161?useUnicode=true&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
   # 用户名
   store.db.user=root
   # 密码
   store.db.password=zerosx@123456
   ```

6. 执行安装

   ```shell
   # 进入目录
   cd doc/seata
   # 安装(指定文件)
   docker-compose -f docker-compose-seata1.6.1.yaml up -d
   ```

   

### 5. 服务库表SQL

​	初始脚本，包括库表创建及初始数据，文件【doc/zerosx/zerosx-sql】

```shell
执行SQL脚本，文件所在目录：doc/zerosx/zerosx-sql
# zerosx_auth.sql 			#auth服务数据库
# zerosx_system.sql			#system服务数据库、菜单目录、初始登录用户
# t_area_city_source.sql	#行政区域初始数据
```

