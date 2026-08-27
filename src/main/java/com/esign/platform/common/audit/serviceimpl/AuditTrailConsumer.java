//package com.esign.platform.common.audit.serviceimpl;
//
//import com.esign.platform.common.audit.config.KafkaConfig;
//import com.esign.platform.common.audit.dto.AuditEvent;
//import com.esign.platform.common.audit.entity.AuditTrail;
//import com.esign.platform.common.audit.repository.AuditTrailRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuditTrailConsumer {
//
//    private final AuditTrailRepository auditTrailRepository;
//
//    @KafkaListener(
//            topics = KafkaConfig.AUDIT_TOPIC,
//            groupId = KafkaConfig.AUDIT_CONSUMER_GROUP,
//            containerFactory = "auditKafkaListenerContainerFactory"
//    )
//    public void consume(AuditEvent event) {
//
//        log.info(
//                "Received audit event: action={}, entityType={}, entityId={}",
//                event.getAction(),
//                event.getEntityType(),
//                event.getEntityId()
//        );
//
//        AuditTrail auditTrail =
//                AuditTrail.builder()
//
//                        .organizationId(
//                                event.getOrganizationId()
//                        )
//
//                        .userId(
//                                event.getUserId()
//                        )
//
//                        .employeeId(
//                                event.getEmployeeId()
//                        )
//
//                        .action(
//                                event.getAction()
//                        )
//
//                        .entityType(
//                                event.getEntityType()
//                        )
//
//                        .entityId(
//                                event.getEntityId()
//                        )
//
//                        .description(
//                                event.getDescription()
//                        )
//
//                        .requestMethod(
//                                event.getRequestMethod()
//                        )
//
//                        .requestUri(
//                                event.getRequestUri()
//                        )
//
//                        .ipAddress(
//                                event.getIpAddress()
//                        )
//
//                        .userAgent(
//                                event.getUserAgent()
//                        )
//
//                        .beforeData(
//                                event.getBeforeData()
//                        )
//
//                        .afterData(
//                                event.getAfterData()
//                        )
//
//                        .status(
//                                event.getStatus()
//                        )
//
//                        .errorMessage(
//                                event.getErrorMessage()
//                        )
//
//                        .correlationId(
//                                event.getCorrelationId()
//                        )
//
//                        .createdAt(
//                                event.getCreatedAt()
//                        )
//
//                        .build();
//
//        auditTrailRepository.save(
//                auditTrail
//        );
//
//        log.info(
//                "Audit event saved successfully: action={}, id={}",
//                event.getAction(),
//                auditTrail.getId()
//        );
//    }
//}