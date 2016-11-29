/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.feel.runtime.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An event class to report an evaluation error occured due to invalid parameters.
 */
public class InvalidParametersEvent
        extends FEELEventBase
        implements FEELEvent {

    private String paramNameInError;
    private String paramProblem;
    private String nodeName;
    private final Map<String, Object> actualParameters = new HashMap<>();
    
    public InvalidParametersEvent(Severity severity, String paramNameInError, String paramProblem) {
        super( severity, null, null );
        this.paramNameInError = paramNameInError;
        this.paramProblem = paramProblem;
    }
    
    @Override
    public String getMessage() {
        return "The parameter '"+paramNameInError+"', in function "+getNodeName()+"(), "+paramProblem+".";
    }

    public InvalidParametersEvent(Severity severity, String msg, String nodeName, Map<String, Object> actualParameterKV) {
        super( severity, msg, null );
        this.nodeName = nodeName;
        if (actualParameterKV != null) 
            this.actualParameters.putAll( actualParameterKV );
    }
    
    public InvalidParametersEvent(Severity severity, String msg, String nodeName, List<String> parameterNames, List<Object> parameterValues) {
        super( severity, msg, null );
        this.nodeName = nodeName;
        setActualParameters(parameterNames, parameterValues);
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }
    
    public void setActualParameters(List<String> parameterNames, List<Object> parameterValues) {
        this.actualParameters.clear();
        IntStream.range(0, parameterNames.size())
            .forEach( idx -> actualParameters.put(parameterNames.get(idx), parameterValues.get(idx)) );
    }

    public Map<String, Object> getActualParameters() {
        return Collections.unmodifiableMap( actualParameters );
    }

    @Override
    public String toString() {
        return "InvalidParametersEvent{" +
               "severity=" + getSeverity() +
               ", nodeName='" + nodeName + '\'' +
               ", message='" + getMessage() + '\'' +
               ", actualParameters='" + formatMap(actualParameters) + '\'' +
               '}';
    }

    private String formatMap(Map<String, Object> actualParameters) {
        try {
            StringBuilder sb = new StringBuilder("{");
            sb.append(
                actualParameters.entrySet().stream()
                    .map(kv -> kv.getKey() + "=" + ( kv.getValue() != null && kv.getValue().getClass().isArray() ? Arrays.deepToString( (Object[]) kv.getValue() ) : kv.getValue() ) )
                    .collect(Collectors.joining(", "))
            );
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<unable to inspect actualParameters map>";
        }
    }
}
