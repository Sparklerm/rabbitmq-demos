# RabbitMQ 延迟队列

在 RabbitMQ 上实现定时任务有两种方式：

- 利用 RabbitMQ 自带的消息过期和死信队列机制，实现定时任务。
- 使用 RabbitMQ 的 rabbitmq_delayed_message_exchange 插件来实现定时任务。

## 插件方式

### 安装插件

首先需要下载 rabbitmq_delayed_message_exchange 插件，这是一个 GitHub
上的开源项目https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases，选择适合自己的版本。

在项目的plugins文件夹中包含了一个3.9版本的插件。

Docker用户（非Docker用户通过sftp工具上传插件后直接走第三步）：

1. copy插件文件到Docker容器中。`cp`后第一个参数：待copy文件 ，第二个参数：容器名或容器ID，第三个参数：目标目录

   ```
   docker cp ./rabbitmq_delayed_message_exchange-3.9.0.ez some-rabbit:/plugins
   ```


2. 进入容器

   ```
   docker exec -it some-rabbit bash
   ```


3. 启用插件

   ```
   # 先进入copy插件的目录下 然后执行以下命令
   rabbitmq-plugins enable rabbitmq_delayed_message_exchange
   ```

## 代码

### 配置文件

```
 public static final String QUEUE_NAME = "delay_queue";
 public static final String EXCHANGE_NAME = "delay_exchange";
 public static final String EXCHANGE_TYPE = "x-delayed-message";

 @Bean
 Queue queue() {
     return new Queue(QUEUE_NAME, true, false, false);
 }

 /**
  * 这里我们使用的交换机是 CustomExchange，这是一个 Spring 中提供的交换机，创建 CustomExchange 时有五个参数，含义分别如下：
  * 交换机名称。
  * 交换机类型，这个地方是固定的。
  * 交换机是否持久化。
  * 如果没有队列绑定到交换机，交换机是否删除。
  * 其他参数。
  * 最后一个 args 参数中，指定了交换机消息分发的类型，这个类型就是 direct、fanout、topic 以及 header 几种，用了哪种类型，将来交换机分发消息就按哪种方式来。
  *
  * @return
  */
 @Bean
 CustomExchange customExchange() {
     Map<String, Object> args = new HashMap<>();
     args.put("x-delayed-type", "direct");
     return new CustomExchange(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, args);
 }

 @Bean
 Binding binding() {
     return BindingBuilder.bind(queue())
             .to(customExchange()).with(QUEUE_NAME).noargs();
 }
```

### 消费者

```
private static final Logger logger = LoggerFactory.getLogger(DelayConsumer.class);

@RabbitListener(queues = RabbitDelayConfig.QUEUE_NAME)
public void delayConsumer(String message) {
  logger.info("Now time : {} ,Delay queue message ---> {}", LocalDateTime.now().toString(), message);
}
```

### 测试代码

```
@Autowired
RabbitTemplate rabbitTemplate;

@Test
public void rabbitDelayTest() throws UnsupportedEncodingException {
    Message msg = MessageBuilder.withBody(("hello 这是一条来自 " + LocalDateTime.now() + " 的消息").getBytes("UTF-8")).setHeader("x-delay", 3000).build();
    rabbitTemplate.convertAndSend(RabbitDelayConfig.EXCHANGE_NAME, RabbitDelayConfig.QUEUE_NAME, msg);
}
```

