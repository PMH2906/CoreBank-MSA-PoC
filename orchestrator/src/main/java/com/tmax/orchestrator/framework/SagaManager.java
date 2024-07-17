package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaManager {

    private final EntityManager entityManager;
    private final ApplicationEventPublisher eventPublisher;

    public <S extends SagaBase> S begin(Class<S> sagaType, ObjectNode payload) {
        try {

            // sagaType.getAnnotation(Saga.class).type() : Saga 클래스에 구현된 @Saga의 type을 String으로 반환
            SagaState state = new SagaState(sagaType.getAnnotation(Saga.class).type(), payload);
            entityManager.persist(state);

            // 특정 Saga 인스턴스는 모두 SagaBase를 상속받음
            // SagaBase의 Event.class, SagaState.class를 파라미터로 받는 생성자를 가져와 새로운 객체를 만듬
            S saga = sagaType.getConstructor(ApplicationEventPublisher.class, EntityManager.class, SagaState.class).newInstance(eventPublisher, entityManager, state);
            saga.advance();
            return saga;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
