# hrm-parent

HRM系统后端

## 远程操作docker部署微服务

在项目中添加Dockermaven插件依赖

```xml
 <build>
        <finalName>app</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.5.4.RELEASE</version>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>1.1.0</version>
                <executions>
                    <execution>
                        <id>build-image</id>
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <imageName>192.168.182.129:5000/${project.artifactId}:${project.version}</imageName>
                    <baseImage>jdk1.8</baseImage>
                    <entryPoint>["java", "-jar","/${project.build.finalName}.jar"]</entryPoint>
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                    <dockerHost>http://192.168.182.129:2375</dockerHost>
                </configuration>
            </plugin>

        </plugins>
    </build>
```

打开docker的远程访问 修改配置文件,

```
vim /lib/systemd/system/docker.service
修改ExecStart
ExecStart=/usr/bin/dockerd --containerd=/run/containerd/containerd.sock

vim /etc/docker/daemon.json
{
  	"registry-mirrors":["https://docker.mirrors.ustc.edu.cn"],
   	"hosts":["192.168.182.129:2375","unix://var/run/docker.sock"],
	"insecure-registries":["192.168.182.129:5000"]
}
```

重新加载配置文件并重启docker

 ```
 systemctl daemon-reload
 systemctl restart docker
 ```

外部访问需要开放指定端口号 查看开放的端口号

 ```
 firewall-cmd --list-ports
 ```

开放端口号

 ```
 firewall-cmd --add-port=80/tcp --permanent --zone=public 
 ```

重启防火墙

 ```
 firewall-cmd --reload
 ```

查看全部端口号

 ```
 netstat -ntlp
 ```

查看指定端口号

 ```
  netstat -ntlp|grep 端口号
 ```

使用mvn命令构建项目

 ```
 mvn install 
 ```

使用docker插件的构建命令构建镜像,并且上传到服务器

 ```
 mvn docker:build -DpushImage
 ```

此时宿主机上就会有这个项目的镜像，可以使用docker images查看就能看到

构建项目容器，启动就完成了

```
docker run -it --name=myspringboot -p 8080:8080 192.168.182.129:5000/springboot-mybatis
```

上面的8080是要开启的端口号，视项目的具体情况而定，我的项目是9003端口，部署时就改成9003 访问192.168.182.129:8080/项目名