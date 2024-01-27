# maven



1.maven的下载 路径配置

2.maven创建工程 创建web工程的三种方式 1.手动创建java工程添加web组件 2.使用javatoweb插件将java转换为web工程 3.创建idea提供的web模板

3.配置到tomcat 与之前所学一样

4.

![image-20240126190847625](E:\github\JAVA\img\image-20240126190847625.png)

5.相关命令  使用cmd到相关module中pom.xml对应的文件夹下打开

mvn compile 编译项目 生成target文件  是编译主要代码的

mvn test-compile 用于编译测试代码

mvn test 运行编译好的测试字节码

mvn clean 删除target文件夹

mvn package





6.在项目中引用自己其他的项目只需要在pom.xml中加入其坐标即可，此时再去打包会产生错误原因是由于其他项目的jar包既不在本地仓库也不再中央仓库。这时需要将其他项目已经打包好的jar包安装到本地仓库输入***mvn install***即可

7.当下载失败时要通过坐标找到jar包所在位置删除

8. ~~~xm
   <scope></scope> 表明了该jar包可以使用的范围 如果没有该标签则表明可以全局使用
   
   	<properties>
           <maven.compiler.source>17</maven.compiler.source>
           <maven.compiler.target>17</maven.compiler.target>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <junit.version>5.9.2</junit.version>
       </properties>
   		
   		<dependency>
               <groupId>org.junit.jupiter</groupId>
               <artifactId>junit-jupiter-api</artifactId>
               <version>${junit.version}</version>
               <scope>test</scope>
           </dependency>
           可以在属性中设置版本号，在依赖中替换  是因为有很多的jar包要求是同一版本的那么为了方便修改时能够统一需要有一个全局的变量维护
           
           
           
            <!-- jdk17 和 war包不匹配 -->
               <plugin>
                   <groupId>org.apache.maven.plugins</groupId>
                   <artifactId>maven-war-plugin</artifactId>
                   <version>3.2.2</version> <!-- 使用最新版本 -->
               </plugin>
               
               
               
                <!--        自定义打包名称-->
           <finalName>maven_web-1.0.war</finalName>
           
           如果有配置文件没有放在指定位置那么就要设置才能打包进去
           <!--设置要打包的资源位置-->
           <resources>
               <resource>
                   <!--设置资源所在目录-->
                   <directory>src/main/java</directory>
                   <includes>
                       <!--设置包含的资源类型-->
                       <include>**/*.xml</include>
                   </includes>
               </resource>
           </resources>
   ~~~

9. 

通过设置坐标的依赖范围(scope)，可以设置 对应jar包的作用范围：编译环境、测试环境、运行环境

| 依赖范围     | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| **compile**  | 编译依赖范围，scope 元素的缺省值。使用此依赖范围的 Maven 依赖，对于三种 classpath 均有效，即该 Maven 依赖在上述三种 classpath 均会被引入。例如，log4j 在编译、测试、运行过程都是必须的。 |
| **test**     | 测试依赖范围。使用此依赖范围的 Maven 依赖，只对测试 classpath 有效。例如，Junit 依赖只有在测试阶段才需要。 |
| **provided** | 已提供依赖范围。使用此依赖范围的 Maven 依赖，只对编译 classpath 和测试 classpath 有效。例如，servlet-api 依赖对于编译、测试阶段而言是需要的，但是运行阶段，由于外部容器已经提供，故不需要 Maven 重复引入该依赖。 |
| runtime      | 运行时依赖范围。使用此依赖范围的 Maven 依赖，只对测试 classpath、运行 classpath 有效。例如，JDBC 驱动实现依赖，其在编译时只需 JDK 提供的 JDBC 接口即可，只有测试、运行阶段才需要实现了 JDBC 接口的驱动。 |
| system       | 系统依赖范围，其效果与 provided 的依赖范围一致。其用于添加非 Maven 仓库的本地依赖，通过依赖元素 dependency 中的 systemPath 元素指定本地依赖的路径。鉴于使用其会导致项目的可移植性降低，一般不推荐使用。 |
| import       | 导入依赖范围，该依赖范围只能与 dependencyManagement 元素配合使用，其功能是将目标 pom.xml 文件中 dependencyManagement 的配置导入合并到当前 pom.xml 的 dependencyManagement 中。 |

9.当下载失败时 要根据gav找到.lastUpdate结尾的缓存文件删除才能继续下载。如果要删除jar包直接删除对应的版本号的文件包即可

10.**传递的原则**

在 A 依赖 B，B 依赖 C 的前提下，C 是否能够传递到 A，取决于 B 依赖 C 时使用的依赖范围以及配置

- B 依赖 C 时使用 compile 范围：可以传递

- B 依赖 C 时使用 test 或 provided 范围：不能传递，所以需要这样的 jar 包时，就必须在需要的地方明确配置依赖才可以。

- B 依赖 C 时，若配置了以下标签，则不能传递

  ~~~xml
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.15</version>
      <optional>true</optional>
  </dependency>
  ~~~

  

**解决依赖冲突（如何选择重复依赖）方式：**

1. 自动选择原则

   - 短路优先原则（第一原则）

     A—>B—>C—>D—>E—>X(version 0.0.1)

     A—>F—>X(version 0.0.2)

     则A依赖于X(version 0.0.2)。

   - 依赖路径长度相同情况下，则“先声明优先”（第二原则）

     A—>E—>X(version 0.0.1)

     A—>F—>X(version 0.0.2)

     在\<depencies>\</depencies>中，先声明的，路径相同，会优先选择！

2. 手动排除

3. ~~~xml
   <dependency>
     <groupId>com.atguigu.maven</groupId>
     <artifactId>pro01-maven-java</artifactId>
     <version>1.0-SNAPSHOT</version>
     <scope>compile</scope>
     <!-- 使用excludes标签配置依赖的排除  -->
     <exclusions>
       <!-- 在exclude标签中配置一个具体的排除 -->
       <exclusion>
         <!-- 指定要排除的依赖的坐标（不需要写version） -->
         <groupId>commons-logging</groupId>
         <artifactId>commons-logging</artifactId>
       </exclusion>
     </exclusions>
   </dependency>
   ~~~

4. 继承概念

   Maven 继承是指在 Maven 的项目中，让一个项目从另一个项目中继承配置信息的机制。继承可以让我们在多个项目中共享同一配置信息，简化项目的管理和维护工作。

5.聚合 简单来讲就是可以一键操作一键操作所有的工程

6.nexus maven私服

请求本地仓库，若本地仓库不存在所需构件，则跳转到第 2 步；
请求 Maven 私服，将所需构件下载到本地仓库，若私服中不存在所需构件，则跳转到第 3 步。
请求外部的远程仓库，将所需构件下载并缓存到 Maven 私服，若外部远程仓库不存在所需构件，则 Maven 直接报错。