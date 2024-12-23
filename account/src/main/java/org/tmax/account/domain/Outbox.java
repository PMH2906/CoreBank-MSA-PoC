package org.tmax.account.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime timestamp;

    @Column(name = "aggregateid")
    private String aggregateId;

    @Column(name = "aggregatetype")
    private String aggregateType;
    private String type;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;

    public Outbox(UUID aggregateId, String aggregateType, String type, JsonNode payload) {
        this.timestamp = LocalDateTime.now();
        this.aggregateId = aggregateId.toString();
        this.aggregateType = aggregateType;
        this.type = type;
        this.payload = payload;
    }
}
