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

package org.kie.dmn.feel.runtime.functions;

import java.math.BigDecimal;
import java.util.List;

import org.kie.dmn.feel.runtime.events.FEELEvent;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.util.Either;

public class SublistFunction
        extends BaseFEELFunction {

    public SublistFunction() {
        super( "sublist" );
    }

    public Either<FEELEvent, List> apply(@ParameterName("list") List list, @ParameterName("start position") BigDecimal start) {
        return apply( list, start, null );
    }

    public Either<FEELEvent, List> apply(@ParameterName("list") List list, @ParameterName("start position") BigDecimal start, @ParameterName("length") BigDecimal length) {
        if ( list == null ) {
            return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }
        if ( start == null ) {
            return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "start", "cannot be null"));
        }
        if ( start.equals( BigDecimal.ZERO ) ) {
            return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "start", "cannot be zero"));
        }
        if ( start.abs().intValue() > list.size() ) {
            return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "start", "is inconsistent with 'list' size"));
        }
        
        if ( start.intValue() > 0 ) {
            int end = length != null ? start.intValue() - 1 + length.intValue() : list.size();
            if ( end > list.size() ) {
                return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "inconsistencies in attempting to create a sublist bigger than the original list"));
            }
            return Either.ofRight( list.subList( start.intValue() - 1, end ) );
        } else {
            int end = length != null ? list.size() + start.intValue() + length.intValue() : list.size();
            if ( end > list.size() ) {
                return Either.ofLeft(new InvalidParametersEvent(Severity.ERROR, "inconsistencies in attempting to create a sublist bigger than the original list"));
            }
            return Either.ofRight( list.subList( list.size() + start.intValue(), end ) );
        }
    }
}
