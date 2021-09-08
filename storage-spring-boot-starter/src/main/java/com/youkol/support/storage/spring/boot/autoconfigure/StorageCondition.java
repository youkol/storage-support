/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.storage.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

/**
 *
 * @author jackiea
 */
class StorageCondition extends SpringBootCondition {

    private static final Logger logger = LoggerFactory.getLogger(StorageCondition.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        ConditionMessage.Builder message = ConditionMessage.forCondition("Storage", sourceClass);
        Environment environment = context.getEnvironment();
        try {
            BindResult<StorageType> specified = Binder.get(environment).bind("youkol.storage.type", StorageType.class);
            if (!specified.isBound()) {
                return ConditionOutcome.match(message.because("automatic storage type"));
            }
            StorageType required = StorageTypeHelper.getType(((AnnotationMetadata) metadata).getClassName());
            if (specified.get() == required) {
                return ConditionOutcome.match(message.because(specified.get() + " storage type"));
            }
        } catch (BindException ex) {
            logger.error("Find storage type error", ex);
        }

        return ConditionOutcome.noMatch(message.because("Unknown storage type"));
    }

}
