//package com.esign.platform.common.audit.aspect;
//
//import com.esign.platform.common.audit.dto.AuditEvent;
//import com.esign.platform.common.audit.service.AuditTrailService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AuditAspect {
//
//    private final AuditTrailService auditTrailService;
//
//    @Around("@annotation(auditAction)")
//    public Object audit(
//            ProceedingJoinPoint joinPoint,
//            AuditAction auditAction
//    ) throws Throwable {
//
//        Object result = null;
//
//        Exception exception = null;
//
//        try {
//
//            result = joinPoint.proceed();
//
//            return result;
//
//        } catch (Exception ex) {
//
//            exception = ex;
//
//            throw ex;
//
//        } finally {
//
//            try {
//
//                publishAuditEvent(
//                        joinPoint,
//                        auditAction,
//                        result,
//                        exception
//                );
//
//            } catch (Exception auditException) {
//
//                /*
//                 * IMPORTANT:
//                 *
//                 * Audit failure should not break
//                 * the user's business operation.
//                 */
//
//                log.error(
//                        "Failed to create audit event. action={}",
//                        auditAction.action(),
//                        auditException
//                );
//            }
//        }
//    }
//
//    private void publishAuditEvent(
//            ProceedingJoinPoint joinPoint,
//            AuditAction auditAction,
//            Object result,
//            Exception exception
//    ) {
//
//        HttpServletRequest request =
//                getCurrentRequest();
//
//        String requestMethod = null;
//
//        String requestUri = null;
//
//        String ipAddress = null;
//
//        String userAgent = null;
//
//        if (request != null) {
//
//            requestMethod =
//                    request.getMethod();
//
//            requestUri =
//                    request.getRequestURI();
//
//            ipAddress =
//                    getClientIp(request);
//
//            userAgent =
//                    request.getHeader("User-Agent");
//        }
//
//        String correlationId =
//                request != null
//                        ? request.getHeader("X-Correlation-Id")
//                        : null;
//
//        if (correlationId == null) {
//
//            correlationId =
//                    UUID.randomUUID().toString();
//        }
//
//        String status =
//                exception == null
//                        ? "SUCCESS"
//                        : "FAILED";
//
//        String errorMessage =
//                exception == null
//                        ? null
//                        : exception.getMessage();
//
//        Object afterData =
//                exception == null
//                        ? extractResponseBody(result)
//                        : null;
//
//        AuditEvent event =
//                AuditEvent.builder()
//
//                        .action(
//                                auditAction.action()
//                        )
//
//                        .entityType(
//                                auditAction.entityType()
//                        )
//
//                        .description(
//                                auditAction.description()
//                        )
//
//                        .requestMethod(
//                                requestMethod
//                        )
//
//                        .requestUri(
//                                requestUri
//                        )
//
//                        .ipAddress(
//                                ipAddress
//                        )
//
//                        .userAgent(
//                                userAgent
//                        )
//
//                        .afterData(
//                                afterData
//                        )
//
//                        .status(
//                                status
//                        )
//
//                        .errorMessage(
//                                errorMessage
//                        )
//
//                        .correlationId(
//                                correlationId
//                        )
//
//                        .createdAt(
//                                Instant.now()
//                        )
//
//                        .build();
//
//        auditTrailService.publish(
//                event
//        );
//    }
//
//    private HttpServletRequest getCurrentRequest() {
//
//        ServletRequestAttributes attributes =
//                (ServletRequestAttributes)
//                        RequestContextHolder.getRequestAttributes();
//
//        if (attributes == null) {
//
//            return null;
//        }
//
//        return attributes.getRequest();
//    }
//
//    private String getClientIp(
//            HttpServletRequest request
//    ) {
//
//        String forwarded =
//                request.getHeader("X-Forwarded-For");
//
//        if (
//                forwarded != null
//                        &&
//                !forwarded.isBlank()
//        ) {
//
//            return forwarded.split(",")[0].trim();
//        }
//
//        String realIp =
//                request.getHeader("X-Real-IP");
//
//        if (
//                realIp != null
//                        &&
//                !realIp.isBlank()
//        ) {
//
//            return realIp;
//        }
//
//        return request.getRemoteAddr();
//    }
//
//    private Object extractResponseBody(
//            Object result
//    ) {
//
//        if (result instanceof ResponseEntity<?> responseEntity) {
//
//            return responseEntity.getBody();
//        }
//
//        return result;
//    }
//}