## 使用spring-boot和bue构建单页面应用

### 背景
在很多后台管理项目中，我们不希望前后端部署分离，因为从部署成本，系统复杂度等来看，这样不是一个很好的做法。
但单页面应用的优势又是一目了然的，所以希望能把spring-boot和单页面集成起来。
本项目是spring-boot和vue单页面应用集成的一个demo。

### 步骤
- 创建一个spring-boot-web项目，为了方便集成。这个项目包含两个子模块。
    - backend：springboot后端
    - front：vue前端项目
- 在front模块中添加pom.xml
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>${frontend-maven-plugin.version}</version>
                <configuration>
                    <installDirectory>target</installDirectory>
                    <nodeVersion>v10.12.0</nodeVersion>
                </configuration>
                <executions>
                    <execution>
                        <id>install node and npm</id>
                        <goals>
                            <goal>install-node-and-npm</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>npm install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>npm run build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <phase>compile</phase>
                        <configuration>
                            <arguments>run build --fix</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>com.mycila</groupId>
                <artifactId>license-maven-plugin</artifactId>
                <version>3.0</version>
                <executions>
                    <execution>
                        <phase>none</phase>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>net.orfjackal.retrolambda</groupId>
                <artifactId>retrolambda-maven-plugin</artifactId>
                <version>2.5.5</version>
                <executions>
                    <execution>
                        <phase>none</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```
    该配置主要是增加一个maven插件，该插件可以构建前端环境并支持前端的脚本命令。其中下面的配置
    ```xml
    <execution>
        <id>npm run build</id>
        <goals>
            <goal>npm</goal>
        </goals>
        <phase>compile</phase>
        <configuration>
            <arguments>run build --fix</arguments>
        </configuration>
    </execution>
    ```
    对应前端项目的构建命令
    
- 前端项目使用的是vue-cli构建，为了方便后面集成，需要特指构建目录。vue.config.js的相关配置如下
    ```json
    module.exports = {
        ...,
        baseUrl: '/',
        assetsDir: 'static',
        outputDir: './target/dist/',
        ...
    }
    ```
    目的就是为了让前端把编译后的文件放在target目录下，这样可以利用mvn编译编译过程清理构建文件

- 在backend后端的pom.xml中添加如下配置
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy Vue.js frontend content</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>src/main/resources/public</outputDirectory>
                            <overwrite>true</overwrite>
                            <resources>
                                <resource>
                                    <directory>${project.parent.basedir}/front/target/dist</directory>
                                    <includes>
                                        <include>static/</include>
                                        <include>index.html</include>
                                        <include>favicon.ico</include>
                                    </includes>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```
    这个插件主要作用是复制资源。该配置就是把刚才前端编译后的静态资源复制到spring-boot项目的resources目录下。

- 在项目中添加一个MainController.java，并添加默认的跟路由
    ```java
    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("forward:/index.html");
    }
    ```

### 编译
- 编译命令```mvn clean install```
- 编译完成后，在backend的resources下面会增加前端资源文件
- 启动backend项目运行
    
### 本地调试
- 为了本地调试方便，可以在vue.config.js里面启动一个proxy-server来代理后端服务

    ```json
    devServer: {
        port: 3000,
        proxy: {
            '/api': {
                'target': "http://localhost:8080",
                'changeOrigin': true
            }
        }
    }
    ```
    进入前端目录front，使用命令```npm run serve```启动本地测试服务，这样就可以代理到后端服务器 
- 打开[http://localhost:3000](http://localhost:3000)

### 注意细节
- 由于单页面应用，在spring-boot容器中运行的时候，前端路由有时候会被服务端接管。所以前端路由模式设置成```hash```可以解决该问题。
- 不要在parent-pom文件中添加编译插件，会导致编译失败。即下面的配置：

    ```xml
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    ```
- 在parent-pom中，module的顺序很重要，即前端的模块一定要放在最前，不然会导致复制资源有问题

    ```xml
    <modules>
        <module>front</module>
        <module>backend</module>
    </modules>
    ```