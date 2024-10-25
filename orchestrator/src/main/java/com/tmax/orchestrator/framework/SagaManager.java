package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.tmax.orchestrator.domain.SagaState;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaManager {

    private final EntityManager entityManager;

    /**
     * SagaBase를 상속받은 특정 Saga 인스턴스와 SagaState를 생성합니다.
     * SagaState를 저장(정확히는 영속성 컨텍스트에 저장)합니다.
     *
     * @params sagaType : SagaBase를 상속받은 특정 Saga 클래스를 입력합니다.
     * @params payload : 입금자명, 입금자 계좌번호, 수신자명, 수신자 계좌번호, 금액을 입력합니다.
     * **/
    public <S extends SagaBase> S begin(Class<S> sagaType, JsonNode payload) {
        try {

            // sagaType.getAnnotation(Saga.class).type() : Saga 클래스에 구현된 @Saga의 type을 String으로 반환
            SagaState state = new SagaState(sagaType.getAnnotation(Saga.class).type(), payload);
            entityManager.persist(state);

            // 특정 Saga 인스턴스는 모두 SagaBase를 상속받음
            // SagaBase의 Event.class, SagaState.class를 파라미터로 받는 생성자를 가져와 새로운 객체를 만듬
            S saga = sagaType.getConstructor(EntityManager.class, SagaState.class).newInstance(entityManager, state);
            saga.advance();
            return saga;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <S extends SagaBase> S find(Class<S> sagaType, UUID sagaId) {
        SagaState state = entityManager.find(SagaState.class, sagaId);

        if (state == null) {
            return null;
        }

        try {
            return sagaType.getConstructor(EntityManager.class, SagaState.class).newInstance(entityManager, state);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
