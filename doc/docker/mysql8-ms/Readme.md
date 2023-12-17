### MySQL主从部署

#### 1. 启动MySQL主从节点

* 给配置文件设置权限，否则配置不生效 ---重要

  ```shell
  chmod 644 /etc/my.cnf
  ```

* 启动主从节点

  ```shell
  # p:project-name 项目名称
  docker-compose -f docker-compose.yaml -p mysql8-ms up -d
  ```

* 查看相关参数是否跟配置文件一致

  ```shell
  # 登录mysql执行或使用第三方客户端，比如max_connections、server_id等配置
  show variables like '%server_id%';
  ```

  

#### 2. 主库Master配置

* 进入主节点容器

  ```shell
  docker exec -it mysql8-master /bin/bash
  ```

* 登录MySQL

  ```shell
  #-u:用户 -p:密码
  mysql -uroot -pzerosx@123456
  ```

* 创建用于主从同步的用户

  ```shell
  CREATE USER 'slave'@'%' IDENTIFIED BY 'slave@123456';
  #需要重新创建时，执行下面这个命令
  DROP USER 'slave'@'%';
  ```

* 授予用户权限

  ```shell
  # 授予slave用户 REPLICATION SLAVE、REPLICATION CLIENT 权限，用于在主、从数据库之间同步数据
  GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
  # 授予全部权限
  GRANT ALL PRIVILEGES ON *.* TO 'slave'@'%';
  ```

* 使授权操作生效

  ```shell
  FLUSH PRIVILEGES;
  ```

* 查看Master节点状态

  ```shell
  show master status;
  # 结果如下，注意表头的File、Position，在从库配置数据同步时要用到
  +------------------+----------+-------------------------------+------------------+-------------------+
  | File             | Position | Binlog_Do_DB                  | Binlog_Ignore_DB | Executed_Gtid_Set |
  +------------------+----------+-------------------------------+------------------+-------------------+
  | mysql-bin.000027 |   971114 | zerosx_resource,zerosx_system |                  |                   |
  +------------------+----------+-------------------------------+------------------+-------------------+
  1 row in set (0.01 sec)
  ```



#### 3. 从库Slave配置

* 进入从节点容器

  ```shell
  docker exec -it mysql8-slave /bin/bash
  ```

  

* 登录MySQL

  ```shell
  mysql -uroot -pzerosx@123456
  ```

* 重置

  ```
  RESET slave;
  ```

  

* 创建复制通道

  ```shell
  # master_host是主数据库容器名称或主机ip，（8.0.23版本及以后）
  # 官方文档：https://dev.mysql.com/doc/refman/8.0/en/change-replication-source-to.html
  change master to master_host='192.168.3.100',master_port=3308, master_user='slave', master_password='slave@123456', master_log_file='mysql-bin.000003', master_log_pos= 855, master_connect_retry=30,get_master_public_key=1;
  
  ```

  

* 开启同步

  ```shell
  # 开启主从同步过程  【停止命令：stop slave;】
  start slave;
  ```

  

* 查看同步状态

  ```shell
  # 查看主从同步状态
  show slave status \G;
  # Slave_IO_Running 和 Slave_SQL_Running 都是Yes的话，就说明主从同步已经配置好了！
  # 如果Slave_IO_Running为Connecting，SlaveSQLRunning为Yes，则说明配置有问题，这时候就要检查配置中哪一步出现问题了哦，可根据Last_IO_Error字段信息排错或谷歌…
  # *************************** 1. row ***************************
  #                Slave_IO_State: Waiting for master to send event
  #                   Master_Host: 192.168.3.100
  #                   Master_User: slave
  #                   Master_Port: 3308
  #                 Connect_Retry: 30
  #               Master_Log_File: mysql-bin.000004
  #           Read_Master_Log_Pos: 588
  #                Relay_Log_File: c598d8402b43-relay-bin.000002
  #                 Relay_Log_Pos: 320
  #         Relay_Master_Log_File: mysql-bin.000004
  #              Slave_IO_Running: Yes
  #             Slave_SQL_Running: Yes
  ```

  

#### 4. 处理主从数据不一致问题

```shell
# 注意：操作的时候停止主库数据写入

# 在从库查看主从同步状态
docker exec -it mysql8-slave /bin/bash
mysql -uroot -pzerosx@123456
show slave status \G;
#Slave_IO_Running: Yes
#Slave_SQL_Running: No

# 1、手动同步主从库数据（在从库容器操作）
# 先在从库停止主从同步
stop slave;
# 导出主库数据
mysqldump -h 192.168.3.100 -P 3308 -uroot -pzerosx@123456 --all-databases > /tmp/all.sql
# 导入到从库
mysql -uroot -pzerosx@123456
source /tmp/all.sql;

# 2、开启主从同步
# 查看主库状态 => 拿到 File 和 Position 字段的值
docker exec -it mysql8-master /bin/bash
mysql -uroot -pzerosx@123456
show master status;
# 从库操作
change master to master_host='192.168.3.100',master_port=3308, master_user='slave', master_password='slave@123456', master_log_file='mysql-bin.000004', master_log_pos= 588, master_connect_retry=30;
start slave;
# 查看主从同步状态
show slave status \G;
#Slave_IO_Running: Yes
#Slave_SQL_Running: Yes
```
