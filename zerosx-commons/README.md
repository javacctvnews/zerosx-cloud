## 组件使用说明文档

> 我们在开发项目时，有很多工具、组件都可用复用，这样可以减少开发的时间、或减少系统BUG，使用时只需要引入到项目中即可。

### 安装

* Maven

  ```java
  # dependencyManagement版本管理
  <dependencyManagement>
      <dependencies>
          <!-- commons的依赖配置-->
          <dependency>
              <groupId>com.zerosx.cloud</groupId>
              <artifactId>zerosx-common-bom</artifactId>
              <version>latest version</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  
  # 在具体的项目中引入
  <dependencies>
      <!--encrypt加解密-->
      <dependency>
          <groupId>com.zerosx.cloud</groupId>
          <artifactId>zerosx-common-encrypt</artifactId>
      </dependency>
      <!--sms短信-->
      <dependency>
          <groupId>com.zerosx.cloud</groupId>
          <artifactId>zerosx-common-sms</artifactId>
      </dependency>
      <!--oss对象存储-->
      <dependency>
          <groupId>com.zerosx.cloud</groupId>
          <artifactId>zerosx-common-oss</artifactId>
      </dependency>
  </dependencies>
  ```

### zerosx-common-encrypt

​	数据安全在我们项目开发中是很常见的一种需求，包括现在国家对数据安全要求也越来越高，在各种招投标上也是有这个需求的。**zerosx-common-encrypt**就是解决这个需求而封装的一个组件，此组件简介如下：

#### 如何使用

1. 自定义加解密算法（如果需要）

   组件内置了**AES**、**SM4**两种加解密算法，如果不满足需求，可自定义加解密算法并实现**IEncryptor**接口或继承**AbsEncryptor**类。

   ```java
   /**
    * 加解密算法器
    */
   public interface IEncryptor {
       /**
        * 加密
        *
        * @param content 需要加密内容
        * @return 加密后内容
        */
       String encrypt(String content);
       /**
        * 解密
        *
        * @param content 需要解密内容
        * @return 解密后内容
        */
       String decrypt(String content);
   }
   ```

2. 实体类和字段加上注解

   * @EncryptClass：实体类上的注解，有此注解才会对字段进行加解密操作

     ```java
     /**
      * 敏感信息类
      * 声明后才会继续扫描注解类下的其它敏感信息注解
      */
     @Inherited
     @Target({ElementType.FIELD, ElementType.TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     public @interface EncryptClass {
     
     }
     ```

   * @EncryptField：实体字段的注解，有此注解才会对字段进行加解密操作

     ```java
     /**
      * 加密字段注解或dao方法
      */
     @Inherited
     @Target({ElementType.METHOD, ElementType.FIELD})
     @Retention(RetentionPolicy.RUNTIME)
     public @interface EncryptField {
         /**
          * 加解密算法类
          */
         Class<? extends IEncryptor> algo() default Sm4Encryptor.class;
         /**
          * 加解密方式
          */
         EncryptMode mode() default EncryptMode.ALL;
         /**
          * 密钥，为空时使用全局密钥
          *
          * @return
          */
         String password() default "";
         /**
          * 字段名，场景：
          * 1）参数是map
          * 2）返回结果是map
          *
          * @return
          */
         String field() default "";
     }
     ```

   * @EncryptFields：DAO接口方法上的注解，应用场景：1）入参是Map类型；2）入参是集合或数组；3）入参是多个字段

     ```java
     /**
      * map入参有多个加密字段
      */
     @Inherited
     @Target({ElementType.METHOD, ElementType.FIELD})
     @Retention(RetentionPolicy.RUNTIME)
     public @interface EncryptFields {
         /**
          * 敏感信息（多个）
          */
         EncryptField[] value() default {};
     }
     ```

     

3. 配置密钥等信息

   ```java
   @Configuration
   public class EncryptConfig {
   	
       @Bean
       @ConfigurationProperties(prefix = "zerosx.encrypt")
       public EncryptProperties encryptProperties(){
           return new EncryptProperties();
       }
   }
   ```

   ```java
   zerosx:
     encrypt:
       enabled: true
       password: "1234443223567887"
       debug: true
   ```

4. 场景测试

   * 完全使用Mybatis-Plus的情况

     ```java
     //保存
     @Test
     public void insert() {
         User user = new User();
         user.setNickName("张三");
         user.setUserName("张三丰");
         user.setPhone("16688889999");
         user.setEmail("16688889999@qq.com");
         user.setCardId("13899000292920202233");
         user.setAddress("武当山");
         userService.save(user);
         userMapper.insert(user);
     }
     
     //更新
     @Test
     public void update() {
         User dbUser = userService.getById(1);
         System.out.println("dbUser = " + JacksonUtil.toJSONString(dbUser));
         if (dbUser != null) {
             dbUser.setPhone("16688883333");
             userService.updateById(dbUser);
         }
         User dbUser2 = userService.getById(1);
         System.out.println("dbUser = " + JacksonUtil.toJSONString(dbUser2));
     }
     
     /*对加密字段全词匹配查询*/
     @Test
     public void query() {
         LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
         qw.eq(User::getUserName, "张三丰");//前端输入明文
         List<User> list = userService.list(qw);
         for (User user : list) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
     }
     
     /*带加解密条件字段的分页查询*/
     @Test
     public void page() {
         Page<User> page = new Page<>(1, 10);
         LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
         qw.eq(User::getUserName, "张三丰");//前端输入明文
         Page<User> pageResult = userMapper.selectPage(page, qw);
         List<User> records = pageResult.getRecords();
         long total = pageResult.getTotal();
         System.out.println("参数：" + JacksonUtil.toJSONString(qw));
         System.out.println("总页数：" + total);
         for (User user : records) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
     }
     ```

   * 自定义DAO方法的情况

     ```java
     @Test
     public void queryDTO() {
         UserDTO userDTO = new UserDTO();
         userDTO.setPhone("16688883333");//前端明文参数
         List<User> users = userMapper.queryList(userDTO);
         //参数不应该发生改变
         System.out.println("参数：" + JacksonUtil.toJSONString(userDTO));
         for (User user : users) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
     }
     
     /*参数参数是集合或数据类型*/
     @Test
     public void queryArray() {
         List<String> phones = new ArrayList<>();
         phones.add("16688883333");
         phones.add("16688883334");
         //入参是集合类型
         List<User> users = userMapper.queryByPhones(phones);
         //参数不应该发生改变
         System.out.println("参数：" + JacksonUtil.toJSONString(phones));
         for (User user : users) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
         String[] phoneArr = new String[]{"16688883333", "16688883334"};
         //入参是数组
         List<User> users2 = userMapper.queryByPhoneArr(phoneArr);
         //参数不应该发生改变
         System.out.println("参数：" + JacksonUtil.toJSONString(phoneArr));
         for (User user : users2) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
     }
     
     @Test
     public void queryPhones() {
         //查询结果是集合（加解密字段）
         List<String> phones = userMapper.queryPhones();
         for (String phone : phones) {
             System.out.println("phone：" + phone);
         }
     }
     
     @Test
     public void queryMap() {
          //查询条件是Map
         Map<String, String> map = new HashMap<>();
         map.put("phone", "16688889999");
         map.put("cardId", "13899000292920202233");
         map.put("email", "16688889999@qq.com");
         map.put("nickName", "三");
         List<User> users = userMapper.queryByMap(map);
         for (User user : users) {
             System.out.println(JacksonUtil.toJSONString(user));
         }
     }
     
     ```

     DAO方法如下：

     ```java
     /**DAO接口加解密示例如下*/
     @Mapper
     public interface IUserMapper extends SuperMapper<User> {
         //入参是对象类型
         List<User> queryList(UserDTO userDTO);
         
         //入参是数据或集合类型
         @EncryptField(mode = EncryptMode.ENC)
         List<User> queryByPhones(List<String> phones);
             
         //入参是数据或集合类型
         @EncryptField(mode = EncryptMode.ENC)
         List<User> queryByPhoneArr(String[] phones);
     
         //查询结果是集合
         @EncryptField(mode = EncryptMode.DEC)
         List<String> queryPhones();
     
         //入参是Map
         @EncryptFields({@EncryptField(field = "phone"),
                 @EncryptField(field = "cardId"),
                 @EncryptField(field = "email", algo = AesEncryptor.class)})
         List<User> queryByMap(Map<String, String> map);
     }
     ```

### zerosx-common-oss

> 集成多家OSS对象存储服务商

#### 如何使用

* 开箱即用模式

  ```java
  @Test
  public void testAliyun() throws Exception {
      AliyunOssConfig config = new AliyunOssConfig();
      config.setAccessKeyId("***************************");
      config.setAccessKeySecret("***************************");
      config.setBucketName("zeros-cloud-oss");
      config.setRegionId("oss-cn-shenzhen");
      config.setEndpoint("oss-cn-shenzhen.aliyuncs.com");
      IOssClientService ossClientService = OSSFactory.createClient(OssTypeEnum.ALIBABA, config);
  
      String fileName = "C:\\Users\\cyh\\Pictures\\desktopImg\\微信图片_20230802205217.png";
      String objectName = IdGenerator.getIdStr() + ".png";
      FileInputStream fileInputStream = new FileInputStream(fileName);
      OssObjectVO objectVO = ossClientService.upload(objectName, fileInputStream);
      if (objectVO != null) {
          log.debug("访问:{}", objectVO.getObjectViewUrl());
      }
  }
  ```

  

* 项目中的使用案例



### zerosx-common-sms

> 集成多家OSS对象存储服务商