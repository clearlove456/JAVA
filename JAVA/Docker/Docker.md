# Docker

## yum换源

```bash
【CentOS更换yum源】
0.sudo mkdir -p /etc/yum.repos.d/ #如果etc下没有该文件 创建一个 相应文件会自动生成
1.cd /etc/yum.repos.d				
2.cp CentOS-Base.repo CentOS-Base.repo.bak	#备份
3.sudo curl -o /etc/yum.repos.d/CentOS-Base.repo\ http://mirrors.aliyun.com/repo/Centos-7.repo
4.yum clean all		# 清除旧的yum缓存
5.yum makecache		# 生成新的yum缓存
6.yum update -y		# 更新软件包
```

## 安装Docker

### 卸载旧版

首先如果系统中已经存在旧的Docker，则先卸载：

~~~bash
yum remove docker \
    docker-client \
    docker-client-latest \
    docker-common \
    docker-latest \
    docker-latest-logrotate \
    docker-logrotate \
    docker-engine \
    docker-selinux 
~~~

### 配置Docker的yum库

首先要安装一个yum工具

~~~bash
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
~~~

安装成功后，执行命令，配置Docker的yum源（已更新为阿里云源）：

~~~bash
sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

sudo sed -i 's+download.docker.com+mirrors.aliyun.com/docker-ce+' /etc/yum.repos.d/docker-ce.repo
~~~

更新yum，建立缓存

~~~bash
sudo yum makecache fast
~~~

### 安装Docker

~~~bash
yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
~~~

### 启动和校验

~~~bash
# 启动Docker
systemctl start docker

# 停止Docker
systemctl stop docker

# 重启
systemctl restart docker

# 设置开机自启
systemctl enable docker

# 执行docker ps命令，如果不报错，说明安装启动成功
docker ps
~~~

### 配置镜像加速

镜像地址可能会变更，如果失效可以百度找最新的docker镜像。

配置镜像步骤如下：

~~~bash
# 创建目录
mkdir -p /etc/docker

# 复制内容，注意把其中的镜像加速地址改成你自己的
tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
        "http://hub-mirror.c.163.com",
        "https://mirrors.tuna.tsinghua.edu.cn",
        "http://mirrors.sohu.com",
        "https://ustc-edu-cn.mirror.aliyuncs.com",
        "https://ccr.ccs.tencentyun.com",
        "https://docker.m.daocloud.io",
        "https://docker.awsl9527.cn"
    ]
}
EOF

# 重新加载配置
systemctl daemon-reload

# 重启Docker
systemctl restart docker
~~~

## Docker基础

### 命令解读

~~~bash
docker run -d \
	--name mysql \
	-p 3306:3306 \
	-e TZ=Asian/Shanghai \
	-e MYSQL_ROOT_PASSWORD=123 \
	mysql
~~~

docker run : 创建并运行一个容器， -d是让容器在后台运行

--name mysql : 给容器起一个名字, 必须唯一

-p 3306:3306 : 设置端口映射

-e KEY=VALUE : 设置环境变量

[repository]:[tag] : 镜像名：版本 不写版本会按照最新的版本下载

### 常见命令

容器是镜像的实例化

~~~bash
docker pull
docker images
docker rmi #删除镜像

docker build
docker save
docker load
docker push

docker run 
docker start #只会启动容器 不会创建新的容器
docker stop

docker ps #查看当前容器的状态
docker rm #删除容器
docker logs #查看容器日志
docker exec #进入容器内部
~~~

### 命令别名

~~~bash
vi ~/.bashrc
~~~

进入该文件添加自己所需要的别名

~~~bash
alias dps = 'docker ps --format "table {{.ID}}\t{{.Image}}\t{{.Ports}}\t{{.Status}}\t{{.Names}}"'
alias dis = 'docker images'
~~~

生效

~~~bash
source ~/.bashrc
~~~

### 数据卷挂载

为什么需要挂载

ex:如果我要在容器内修改一些文件 但是容器内是一个最简单的系统配置没有任何的工具 我们不方便在容器内做修改的操作因此需要将容器内的文件挂载到容器外部 使得修改更加的方便

数据卷（Volume）是一个虚拟目录，是容器内n目录与宿主机目录之间映射的桥梁

创建的数据卷都会在/var/lib/docker/volumes 文件夹下

数据卷挂在只有在创建的时候 当创建完毕之后是不能再挂载的

~~~bash
docker volume ls #查看所有的挂载的卷
docker volume inspect **  #**为卷的名字  查看该卷的虚拟机位置
~~~

~~~bash
docker run -d --name nginx -p 80:80 -v html:/usr/share/nginx/html   #卷挂载

docker run -d --name nginx -p 80:80 -v ./mysql:/usr/share/nginx/html  #本挂载  只有./ 或者 /才算是再本地文件夹 否则会被当作数据卷
~~~

### 自定义镜像

镜像结构：Docker的镜像是分层的，当运行命令拉取镜像时会分层的去拉取，如果在仓库中已经有了那么不会再重新下载

Dockerfile可以用来构建镜像

### 网络

~~~bash
docker network creat #创建一个网络
docker network ls #查看所有网络
docker network rm #删除指定网络
docker network prune #清除未指定网络
docker network connect #使指定容器连接加入某网络
docker network  disconnect #使指定容器离开某网络
docker network inspect #查看网络详情
~~~

