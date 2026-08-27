//package com.esign.platform.common.audit.serviceimpl;
//
//import com.esign.platform.common.audit.config.KafkaConfig;
//import com.esign.platform.common.audit.dto.AuditEvent;
//import com.esign.platform.common.audit.service.AuditTrailService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuditTrailServiceImpl
//        implements AuditTrailService {
//
//    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
//
//    @Override
//    public void publish(AuditEvent event) {
//
//        String key;
//
//        if (event.getEntityId() != null) {
//            key = event.getEntityId().toString();
//        } else if (event.getUserId() != null) {
//            key = event.getUserId().toString();
//        } else {
//            key = event.getAction();
//        }
//
//        kafkaTemplate.send(
//                KafkaConfig.AUDIT_TOPIC,
//                key,
//                event
//        ).whenComplete(
//                (result, exception) -> {
//
//                    if (exception != null) {
//
//                        log.error(
//                                "Failed to publish audit event. action={}",
//                                event.getAction(),
//                                exception
//                        );
//
//                        return;
//                    }
//
//                    log.debug(
//                            "Audit event published successfully. action={}, topic={}, partition={}, offset={}",
//                            event.getAction(),
//                            result.getRecordMetadata().topic(),
//                            result.getRecordMetadata().partition(),
//                            result.getRecordMetadata().offset()
//                    );
//                }
//        );
//    }
//}