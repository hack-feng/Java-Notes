# Apache Kafka #

 *  采用 Scala 语言开发的一个分布式、多分区、多副本且基于 ZooKeeper 协调的用于**处理数据流**的分布式消息框架
 *  消息生产者向 Kafka 服务器发送消息，Kafka 接收消息后，再投递给消费者
 *  在 Kafka 中，生产者的消息会被发送到 Topic 中，Topic 中保存着各类数据，每一条数据都使用键、值进行保存
 *  每一个 Topic 中都包含一个或多个物理分区（Partition)，这些分区维护着消息的内容和索引，它们有可能被保存在不同的服务器中
 *  消费者会为自己添加一个**消费者组**的标识，每一条发布到 Topic 的记录，都会被交付给消费者组的**一个**消费者实例
 *  如果多个消费者实例拥有相同的消费者组，那么这些记录将会**分配**到各个消费者实例上，以达到负载均衡的目的
 *  如果所有的消费者都有不同的消费者组，那么每一条记录都会被**广播**到全部的消费者进行处理

# 通过 Docker 启动 Kafka #

 *  官方指引
    
     *  https://hub.docker.com/r/confluentinc/cp-kafka
     *  https://docs.confluent.io/current/quickstart/cos-docker-quickstart.html
 *  运行镜像
    
     *  https://github.com/confluentinc/cp-docker-images，kafka-single-node/docker-compose.yml
     *  docker-compose up -d