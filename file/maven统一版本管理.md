在使用Maven多模块结构工程时，配置版本是一个比较头疼的事。继承版本，依赖版本，自身版本，都需要单独定义，很是麻烦。但其实Maven已经提供了这种CI版本的管理方式，下面来介绍具体用法。

## **单模块项目[#](https://link.zhihu.com/?target=https%3A//www.cnblogs.com/ElEGenT/p/12938773.html%23659078846)**

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache</groupId>
    <artifactId>apache</artifactId>
    <version>18</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-parent</artifactId>
  <name>First CI Friendly</name>
  <version>${revision}</version>
  ...
</project>
```

这种情况比较简单，只使用了`${revision}`来替换版本。

还可以用另一种动态添加参数的方式来指定版本

```text
$ mvn -Drevision=1.0.0-SNAPSHOT clean package
```

-D 代表设置环境变量

```text
-D,--define <arg> Define a system property
```

或者在(父)项目的properties中定义版本：

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache</groupId>
    <artifactId>apache</artifactId>
    <version>18</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-parent</artifactId>
  <name>First CI Friendly</name>
  <version>${revision}</version>
  ...
  <properties>
    <revision>1.0.0-SNAPSHOT</revision>
  </properties>
</project>
```

## **多模块项目[#](https://link.zhihu.com/?target=https%3A//www.cnblogs.com/ElEGenT/p/12938773.html%23951542804)**

现在来看看多模块构建的情况。有一个父项目和一个或多子模块。父pom将如下所示：

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache</groupId>
    <artifactId>apache</artifactId>
    <version>18</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-parent</artifactId>
  <name>First CI Friendly</name>
  <version>${revision}</version>
  ...
  <properties>
    <revision>1.0.0-SNAPSHOT</revision>
  </properties>
  <modules>
    <module>child1</module>
    ..
  </modules>
</project>
```

子模块配置：

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache.maven.ci</groupId>
    <artifactId>ci-parent</artifactId>
    <version>${revision}</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-child</artifactId>
   ...
</project>
```

多模块项目中子模块的版本应该使用父工程的版本，单独设置版本的话会导致版本混乱。

## **依赖**

多模块工程结构下，会有很多模块依赖的情况，应该使用${project.version}来定义依赖（同父工程下的依赖）的版本

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache</groupId>
    <artifactId>apache</artifactId>
    <version>18</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-parent</artifactId>
  <name>First CI Friendly</name>
  <version>${revision}</version>
  ...
  <properties>
    <revision>1.0.0-SNAPSHOT</revision>
  </properties>
  <modules>
    <module>child1</module>
    ..
  </modules>
</project>
```

子工程配置：

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache.maven.ci</groupId>
    <artifactId>ci-parent</artifactId>
    <version>${revision}</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-child</artifactId>
   ...
  <dependencies>
        <dependency>
      <groupId>org.apache.maven.ci</groupId>
      <artifactId>child2</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
</project>
```

定义依赖版本时，使用${revision}会导致构建失败，需要使用${project.version}。

## **install/deploy**

如果使用以上设置来发布，必须使用flatten-maven-plugin

```text
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache</groupId>
    <artifactId>apache</artifactId>
    <version>18</version>
  </parent>
  <groupId>org.apache.maven.ci</groupId>
  <artifactId>ci-parent</artifactId>
  <name>First CI Friendly</name>
  <version>${revision}</version>
  ...
  <properties>
    <revision>1.0.0-SNAPSHOT</revision>
  </properties>

 <build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>flatten-maven-plugin</artifactId>
      <version>1.1.0</version>
      <configuration>
          <!-- 是否更新pom文件，此处还有更高级的用法 -->
        <updatePomFile>true</updatePomFile>
        <flattenMode>resolveCiFriendliesOnly</flattenMode>
      </configuration>
      <executions>
        <execution>
          <id>flatten</id>
          <phase>process-resources</phase>
          <goals>
            <goal>flatten</goal>
          </goals>
        </execution>
        <execution>
          <id>flatten.clean</id>
          <phase>clean</phase>
          <goals>
            <goal>clean</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
  </build>
  <modules>
    <module>child1</module>
    ..
  </modules>
</project>
```

最终执行 mvn -Drevision=1.0.0-SNAPSHOT clean`install/deploy`后，会将该模块的pom文件中的`${revision}`替换为实际的版本。

`install/deploy`后会在项目目录下生成文件:`.flattened-pom.xml` , 这是替换版本号后的`pom.xml`文件 , 也是最终打进 jar 包里的文件 .