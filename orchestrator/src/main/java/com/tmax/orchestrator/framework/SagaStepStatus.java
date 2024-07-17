/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.tmax.orchestrator.framework;

public enum SagaStepStatus {
    STARTED, FAILED, SUCCEEDED, COMPENSATING, COMPENSATED;
}
