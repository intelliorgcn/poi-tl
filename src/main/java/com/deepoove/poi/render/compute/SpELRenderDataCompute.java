/*
 * Copyright 2014-2020 Sayi
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
package com.deepoove.poi.render.compute;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Spring expression language compute
 * 
 * @author Sayi
 * @since 1.5.0
 */
public class SpELRenderDataCompute implements RenderDataCompute {

    private final ExpressionParser parser;
    private final EvaluationContext context;
    private boolean isStrict;

    public SpELRenderDataCompute(Object root) {
        this(root, true, Collections.emptyMap());
    }

    public SpELRenderDataCompute(Object root, boolean isStrict) {
        this(root, isStrict, Collections.emptyMap());
    }

    public SpELRenderDataCompute(Object root, boolean isStrict, Map<String, Method> spELFunction) {
        this.isStrict = isStrict;
        parser = new SpelExpressionParser();
        context = new StandardEvaluationContext(root);
        ((StandardEvaluationContext) context).addPropertyAccessor(new ReadMapAccessor());
        spELFunction.forEach(((StandardEvaluationContext) context)::registerFunction);
    }

    @Override
    public Object compute(String el) {
        try {
            return parser.parseExpression(el).getValue(context);
        } catch (Exception e) {
            if (isStrict) throw e;
            return null;
        }
    }

}
