### MySQL - 主从同步

```shell

# 首先给配置文件设置权限，否则后面的配置都不会生效(mysql8必须操作，master和slave都要执行) ----重要
chmod  644 /etc/my.cnf

# -p:project-name 项目名称
docker-compose -f docker-compose.yaml -p mysql8-master-slave up -d

# 查看数据库的service_id的值，可以用作检查配置是否生效
show variables like 'server_id';

```

```shell
# ================== 配置主库master start ==================
# 进入主库
docker exec -it mysql8-master /bin/bash
# 登录mysql
mysql -uroot -pzerosx@123456
#  创建用户slave，密码123456  删除：DROP USER 'slave'@'%';
CREATE USER 'slave'@'%' IDENTIFIED BY 'slave@123456';
# 授予slave用户 `REPLICATION SLAVE`权限和`REPLICATION CLIENT`权限，用于在`主` `从` 数据库之间同步数据
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
# 授予所有权限则执行命令: GRANT ALL PRIVILEGES ON *.* TO 'slave'@'%';
# 使操作生效
FLUSH PRIVILEGES;
# 查看状态
show master status;
# 注：File和Position字段的值slave中将会用到，在slave操作完成之前不要操作master，否则将会引起状态变化，即File和Position字段的值变化 !!!
# +------------------+----------+--------------+------------------+-------------------+
# | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
# +------------------+----------+--------------+------------------+-------------------+
# | mysql-bin.000003 |     855  |              |                  |                   |
# +------------------+----------+--------------+------------------+-------------------+
# 1 row in set (0.00 sec)

# ================== 配置主库master end =====================

# ==================  配置从库slave start  ====================
# 进入从库
docker exec -it mysql8-slave /bin/bash
# 登录mysql
mysql -uroot -pzerosx@123456
# 首先停止数据同步相关的线程： slave I/O 线程和 slave SQL 线程
stop slave;
# 为了避免可能发生的错误，直接重置客户端
reset slave;
# master_host是主数据库容器名称或主机ip
change master to master_host='192.168.3.100',master_port=3308, master_user='slave', master_password='slave@123456', master_log_file='mysql-bin.000003', master_log_pos= 855, master_connect_retry=30,get_master_public_key=1;

# 开启主从同步过程  【停止命令：stop slave;】
start slave;
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
#               Replicate_Do_DB:
```

# ==================  配置从库slave end  ====================

###### 解决主从同步数据不一致问题

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
