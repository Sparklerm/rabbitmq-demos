# TTL（Time-To-Live）

消息存活的时间，即消息的有效期。如果我们希望消息能够有一个存活时间，那么我们可以通过设置 TTL 来实现这一需求。如果消息的存活时间超过了 TTL 并且还没有被消息，此时消息就会变成`死信`。

## TTL 的设置有两种不同的方式：

1. 在声明队列的时候，我们可以在队列属性中设置消息的有效期，这样所有进入该队列的消息都会有一个相同的有效期。

   配置文件：
   ```
    public static final String QUEUE_NAME_1 = "ttl_queue_1";
    public static final String EXCHANGE_NAME = "ttl_exchange";
    public static final String HELLO_ROUTING_KEY = "ttl_routing_key";

    @Bean
    Queue queue_1() {
        return new Queue(QUEUE_NAME_1, true, false, false);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding binding_1() {
        return BindingBuilder.bind(queue_1())
                .to(directExchange())
                .with(HELLO_ROUTING_KEY);
    }
   ```

   消息发送代码：
   ```
   @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 方式一，在发送消息是声明消息的有效期。
     */
    @Test
    void method1() {
        // 时间单位为毫秒
        Message message = MessageBuilder.withBody("hello ttl queue demo1".getBytes())
                .setExpiration("10000")
                .build();
        rabbitTemplate.convertAndSend(RabbitTTLConfig.QUEUE_NAME_1, message);
    }
   ```


2. 在发送消息的时候设置消息的有效期，这样不同的消息就具有不同的有效期。

   配置文件：
   ```
    public static final String QUEUE_NAME_2 = "ttl_queue_2";
    public static final String EXCHANGE_NAME = "ttl_exchange";
    public static final String HELLO_ROUTING_KEY = "ttl_routing_key";

    /**
     * 方式二，在声明队列时设置队列消息的有效期。通过此方式创建的队列在控制面板可见TTL标识。
     *
     * @return
     */
    @Bean
    Queue queue_2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        return new Queue(QUEUE_NAME_2, true, false, false, args);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding binding_2() {
        return BindingBuilder.bind(queue_2())
                .to(directExchange())
                .with(HELLO_ROUTING_KEY);
    }
   ```
   发送消息：
   ```
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 方式二，在发送消息时不用再设置有效期。
     */
    @Test
    void method2() {
        // 时间单位为毫秒
        Message message = MessageBuilder.withBody("hello ttl queue demo2".getBytes())
                .build();
        rabbitTemplate.convertAndSend(RabbitTTLConfig.QUEUE_NAME_2, message);
    }
   ```

那如果两个都设置了呢？
**以时间短的为准。**
当我们设置了消息有效期后，消息过期了就会被从队列中删除了（进入到死信队列，后文一样，不再标注），但是两种方式对应的删除时机有一些差异：

1. 对于第一种方式，当消息队列设置过期时间的时候，那么消息过期了就会被删除，因为消息进入 RabbitMQ 后是存在一个消息队列中，队列的头部是最早要过期的消息，所以 RabbitMQ
   只需要一个定时任务，从头部开始扫描是否有过期消息，有的话就直接删除。
2.

对于第二种方式，当消息过期后并不会立马被删除，而是当消息要投递给消费者的时候才会去删除，因为第二种方式，每条消息的过期时间都不一样，想要知道哪条消息过期，必须要遍历队列中的所有消息才能实现，当消息比较多时这样就比较耗费性能，因此对于第二种方式，当消息要投递给消费者的时候才去删除。

**特殊情况**
还有一种特殊情况，就是将消息的过期时间 TTL 设置为 0，这表示如果消息不能立马消费则会被立即丢掉，这个特性可以部分替代 RabbitMQ3.0 以前支持的 immediate 参数，之所以所部分代替，是因为 immediate
参数在投递失败会有 basic.return 方法将消息体返回（这个功能可以利用死信队列来实现）。

# 死信队列

死信交换机，Dead-Letter-Exchange 即 DLX 死信交换机用来接收死信消息（Dead Message）的，那什么是死信消息呢？一般消息变成死信消息有如下几种情况：

- 消息被拒绝(Basic.Reject/Basic.Nack) ，井且设置requeue 参数为false
- 消息过期
- 队列达到最大长度

当消息在一个队列中变成了死信消息后，此时就会被发送到 DLX，绑定 DLX 的消息队列则称为死信队列。

DLX 本质上也是一个普普通通的交换机，我们可以为任意队列指定 DLX，当该队列中存在死信时，RabbitMQ 就会自动的将这个死信发布到 DLX 上去，进而被路由到另一个绑定了 DLX 的队列上（即死信队列）。

配置文件：

```
public static final String DLX_EXCHANGE_NAME = "dlx_exchange";
public static final String DLX_QUEUE_NAME = "dlx_queue";
public static final String DLX_ROUTING_KEY = "dlx_routing_key";
public static final String TTL_QUEUE_NAME = "ttl_dlx_queue";

 /**
  * 配置死信交换机
  *
  * @return
  */
 @Bean
 DirectExchange dlxDirectExchange() {
     return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
 }

 /**
  * 配置死信队列
  *
  * @return
  */
 @Bean
 Queue dlxQueue() {
     return new Queue(DLX_QUEUE_NAME);
 }

 /**
  * 绑定死信队列和死信交换机
  *
  * @return
  */
 @Bean
 Binding dlxBinding() {
     return BindingBuilder.bind(dlxQueue())
             .to(dlxDirectExchange())
             .with(DLX_ROUTING_KEY);
 }

 /**
  * 为消息队列配置死信交换机
  * x-dead-letter-exchange：配置死信交换机。
  * x-dead-letter-routing-key：配置死信 `routing_key`。
  * 发送到这个消息队列上的消息，如果发生了 nack、reject 或者过期等问题，就会被发送到 DLX 上，进而进入到与 DLX 绑定的消息队列上。
  *
  * @return
  */
 @Bean
 Queue queue() {
     Map<String, Object> args = new HashMap<>();
     //设置消息过期时间
     args.put("x-message-ttl", 0);
     //设置死信交换机
     args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
     //设置死信 routing_key
     args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
     return new Queue(TTL_QUEUE_NAME, true, false, false, args);
 }
```

消费者代码：

```
@RabbitListener(queues = RabbitDLXConfig.DLX_QUEUE_NAME)
public void dlxConsumer(String message) {
  System.out.println(RabbitDLXConfig.DLX_QUEUE_NAME + "  ----->  " + message);
}
```

测试代码：

```
@Autowired
RabbitTemplate rabbitTemplate;

@Test
public void send() {
  Message message = MessageBuilder.withBody("This is dlx exchange test message ! ".getBytes())
          .build();
  rabbitTemplate.convertAndSend(RabbitDLXConfig.TTL_QUEUE_NAME, message);
}
```