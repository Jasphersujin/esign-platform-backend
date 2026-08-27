//package com.esign.platform.common.audit.config;
//
////import java.util.HashMap;
////import java.util.Map;
////
////import org.apache.kafka.clients.admin.NewTopic;
////import org.apache.kafka.clients.consumer.ConsumerConfig;
////import org.apache.kafka.common.serialization.StringDeserializer;
////import org.apache.kafka.common.serialization.StringSerializer;
////import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.kafka.annotation.EnableKafka;
////import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
////import org.springframework.kafka.core.ConsumerFactory;
////import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
////import org.springframework.kafka.core.DefaultKafkaProducerFactory;
////import org.springframework.kafka.core.KafkaTemplate;
////import org.springframework.kafka.core.ProducerFactory;
////import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
////import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
////
////import com.esign.platform.common.audit.dto.AuditEvent;
//
//import com.esign.platform.common.audit.dto.AuditEvent;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
//import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//public class KafkaConfig {
//
//    public static final String AUDIT_TOPIC = "audit-trail-events";
//
//    public static final String AUDIT_CONSUMER_GROUP =
//            "audit-trail-consumer-group";
//
//    private final KafkaProperties kafkaProperties;
//
//    public KafkaConfig(KafkaProperties kafkaProperties) {
//        this.kafkaProperties = kafkaProperties;
//    }
//
//    /**
//     * Kafka Producer
//     */
//    @Bean
//    public ProducerFactory<String, AuditEvent> auditProducerFactory() {
//
//        Map<String, Object> properties =
//                new HashMap<>(
//                        kafkaProperties.buildProducerProperties()
//                );
//
//        properties.put(
//                org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class
//        );
//
//        properties.put(
//                org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                JacksonJsonSerializer.class
//        );
//
//        return new DefaultKafkaProducerFactory<>(properties);
//    }
//
//    /**
//     * Kafka Template
//     */
//    @Bean
//    public KafkaTemplate<String, AuditEvent> auditKafkaTemplate() {
//
//        return new KafkaTemplate<>(
//                auditProducerFactory()
//        );
//    }
//
//    /**
//     * Kafka Consumer
//     */
//    @Bean
//    public ConsumerFactory<String, AuditEvent> auditConsumerFactory() {
//
//        JacksonJsonDeserializer<AuditEvent> deserializer =
//                new JacksonJsonDeserializer<>(
//                        AuditEvent.class
//                );
//
//        deserializer.addTrustedPackages(
//                "com.esign.platform.common.audit.dto"
//        );
//
//        Map<String, Object> properties =
//                new HashMap<>(
//                        kafkaProperties.buildConsumerProperties()
//                );
//
//        properties.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class
//        );
//
//        properties.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                JacksonJsonDeserializer.class
//        );
//
//        properties.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                AUDIT_CONSUMER_GROUP
//        );
//
//        return new DefaultKafkaConsumerFactory<>(
//                properties,
//                new StringDeserializer(),
//                deserializer
//        );
//    }
//
//    /**
//     * Kafka Listener Container
//     */
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, AuditEvent>
//    auditKafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, AuditEvent>
//                factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(
//                auditConsumerFactory()
//        );
//
//        factory.getContainerProperties()
//                .setAckMode(
//                        org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD
//                );
//
//        return factory;
//    }
//
//    /**
//     * Kafka Topic
//     */
//    @Bean
//    public NewTopic auditTrailTopic() {
//
//        return new NewTopic(
//                AUDIT_TOPIC,
//                3,
//                (short) 1
//        );
//    }
//}