package org.kie.dmn.core.util;

import org.kie.dmn.api.core.DMNMessageType;

public class Msg {
    // consolidated
    public static final Message2 UNSUPPORTED_ELEMENT                 = new Message2( DMNMessageType.UNSUPPORTED_ELEMENT, "Element %s with type='%s' is not supported." );
    public static final Message2 REQ_INPUT_NOT_FOUND_FOR_NODE        = new Message2( DMNMessageType.REQ_NOT_FOUND, "Required input '%s' not found on node '%s'" );
    public static final Message2 REQ_DECISION_NOT_FOUND_FOR_NODE     = new Message2( DMNMessageType.REQ_NOT_FOUND, "Required Decision '%s' not found on node '%s'" );
    public static final Message2 REQ_BKM_NOT_FOUND_FOR_NODE          = new Message2( DMNMessageType.REQ_NOT_FOUND, "Required Business Knowledge Model '%s' not found on node '%s'" );
    public static final Message2 UNKNOWN_TYPE_REF_ON_NODE            = new Message2( DMNMessageType.TYPE_DEF_NOT_FOUND, "Unable to resolve type reference '%s' on node '%s'" );
    public static final Message2 INVALID_NAME                        = new Message2( DMNMessageType.INVALID_NAME, "Invalid name '%s': %s" );
    public static final Message1 INVALID_SYNTAX                      = new Message1( DMNMessageType.INVALID_SYNTAX, "%s: invalid syntax" );
    public static final Message2 INVALID_SYNTAX2                     = new Message2( DMNMessageType.INVALID_SYNTAX, "%s: %s" );
    public static final Message1 MISSING_EXPRESSION_FOR_BKM          = new Message1( DMNMessageType.MISSING_EXPRESSION, "Missing expression for Business Knowledge Model node '%s'" );
    public static final Message1 MISSING_EXPRESSION_FOR_DECISION     = new Message1( DMNMessageType.MISSING_EXPRESSION, "Missing expression for Decision Node '%s'" );
    public static final Message1 MISSING_EXPRESSION_FOR_NODE         = new Message1( DMNMessageType.MISSING_EXPRESSION, "Missing expression for Node '%s'" );
    public static final Message2 MISSING_EXPRESSION_FOR_INVOCATION   = new Message2( DMNMessageType.MISSING_EXPRESSION, "Missing expression for parameter %s on node '%s'" );
    public static final Message1 MISSING_PARAMETER_FOR_INVOCATION    = new Message1( DMNMessageType.MISSING_EXPRESSION, "Missing parameter for invocation node '%s'" );
    public static final Message2 MISSING_EXPRESSION_FOR_NAME         = new Message2( DMNMessageType.MISSING_EXPRESSION, "No expression defined for name '%s' on node '%s'" );
    public static final Message1 MISSING_VARIABLE_FOR_BKM            = new Message1( DMNMessageType.MISSING_VARIABLE, "Business Knowledge Model node '%s' is missing the variable declaration" );
    public static final Message1 MISSING_VARIABLE_FOR_INPUT          = new Message1( DMNMessageType.MISSING_VARIABLE, "Input Data node '%s' is missing the variable declaration" );
    public static final Message1 MISSING_VARIABLE_FOR_DECISION       = new Message1( DMNMessageType.MISSING_VARIABLE, "Decision node '%s' is missing the variable declaration" );
    public static final Message2 VARIABLE_NAME_MISMATCH_FOR_BKM      = new Message2( DMNMessageType.VARIABLE_NAME_MISMATCH, "Variable name '%s' does not match the Business Knowledge Model node name '%s'" );
    public static final Message2 VARIABLE_NAME_MISMATCH_FOR_DECISION = new Message2( DMNMessageType.VARIABLE_NAME_MISMATCH, "Variable name '%s' does not match the Decision node name '%s'" );
    public static final Message2 VARIABLE_NAME_MISMATCH_FOR_INPUT    = new Message2( DMNMessageType.VARIABLE_NAME_MISMATCH, "Variable name '%s' does not match the Input Data node name '%s'" );

    // not consolidated yet
    public static final Message1 DECISION_NOT_FOUND_FOR_NAME                         = new Message1( DMNMessageType.DECISION_NOT_FOUND, "Decision not found for name '%s'" );
    public static final Message1 DECISION_NOT_FOUND_FOR_ID                           = new Message1( DMNMessageType.DECISION_NOT_FOUND, "Decision not found for type '%s'" );
    public static final Message2 MISSING_DEP_FOR_BKM                                 = new Message2( DMNMessageType.MISSING_DEP, "Missing dependency for Business Knowledge Model node '%s': dependency='%s'" );
    public static final Message2 ERROR_EVAL_BKM_NODE                                 = new Message2( DMNMessageType.ERROR_EVAL_NODE, "Error evaluating Business Knowledge Model node '%s': %s" );
    public static final Message2 MISSING_DEP_FOR_DECISION                            = new Message2( DMNMessageType.MISSING_DEP, "Missing dependency for Decision node '%s': dependency='%s'" );
    public static final Message2 ERROR_EVAL_DECISION_NODE                            = new Message2( DMNMessageType.ERROR_EVAL_NODE, "Error evaluating Decision node '%s': %s" );
    public static final Message2 UNABLE_TO_EVALUATE_DECISION_AS_IT_DEPS              = new Message2( DMNMessageType.MISSING_DEP, "Unable to evaluate decision '%s' as it depends on decision '%s'" );
    public static final Message2 EXPR_TYPE_NOT_SUPPORTED_IN_NODE                     = new Message2( DMNMessageType.EXPR_TYPE_NOT_SUPPORTED_IN_NODE, "Expression type '%s' not supported in node '%s'" );
    public static final Message3 ERR_COMPILING_FEEL_EXPR_ON_DT_INPUT_CLAUSE_IDX      = new Message3( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling FEEL expression '%s' on decision table '%s', input clause #%s" );
    public static final Message3 ERR_COMPILING_FEEL_EXPR_ON_DT_OUTPUT_CLAUSE_IDX     = new Message3( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling FEEL expression '%s' on decision table '%s', output clause #%s" );
    public static final Message3 ERR_COMPILING_FEEL_EXPR_ON_DT_RULE_IDX              = new Message3( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling FEEL expression '%s' on decision table '%s', rule #%s" );
    public static final Message2 ERR_COMPILING_FEEL_EXPR_ON_DT                       = new Message2( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling FEEL expression '%s' on decision table '%s'" );
    public static final Message2 ERR_COMPILING_ALLOWED_VALUES_LIST_ON_ITEM_DEF       = new Message2( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling allowed values list '%s' on item definition '%s'" );
    public static final Message3 ERR_COMPILING_FEEL_EXPR_FOR_NAME_ON_NODE            = new Message3( DMNMessageType.ERR_COMPILING_FEEL, "Error compiling FEEL expression '%s' for name '%s' on node '%s'" );
    public static final Message2 ERR_EVAL_CTX_ENTRY_ON_CTX                           = new Message2( DMNMessageType.ERR_EVAL_CTX, "Error evaluating context extry '%s' on context '%s'" );
    public static final Message1 FEEL_ERROR                                          = new Message1( DMNMessageType.FEEL_PROBLEM, "%s" );
    public static final Message1 FEEL_WARN                                           = new Message1( DMNMessageType.FEEL_PROBLEM, "%s" );
    public static final Message2 FUNCTION_NOT_FOUND_INVOCATION_FAILED_ON_NODE        = new Message2( DMNMessageType.MISSING_DEP, "Function '%s' not found. Invocation failed on node '%s'" );
    public static final Message3 ERR_EVAL_PARAM_FOR_INVOCATION_ON_NODE               = new Message3( DMNMessageType.ERR_EVAL, "Error evaluating parameter '%s' for invocation '%s' on node '%s'" );
    public static final Message2 ERR_INVOKING_PARAM_EXPR_FOR_PARAM_ON_NODE           = new Message2( DMNMessageType.ERR_INVOKE, "Error invoking parameter expression for parameter '%s' on node '%s'." );
    public static final Message2 ERR_INVOKING_FUNCTION_ON_NODE                       = new Message2( DMNMessageType.ERR_INVOKE, "Error invoking function '%s' on node '%s'" );
    public static final Message2 ERR_EVAL_LIST_ELEMENT_ON_POSITION_ON_LIST           = new Message2( DMNMessageType.ERR_EVAL, "Error evaluating list element on position '%s' on list '%s'" );
    public static final Message3 ERR_EVAL_ROW_ELEMENT_ON_POSITION_ON_ROW_OF_RELATION = new Message3( DMNMessageType.ERR_EVAL, "Error evaluating row element on position '%s' on row '%s' of relation '%s'" );

    public static final Message0 CONTEXT_DUP_ENTRY                   = new Message0( DMNMessageType.CONTEXT_DUP_ENTRY, "Context contains duplicate context entry keys" );
    public static final Message0 CONTEXT_ENTRY_NOTYPEREF             = new Message0( DMNMessageType.CONTEXT_ENTRY_NOTYPEREF, "Context entry is missing typeRef" );
    public static final Message0 DRGELEM_NOT_UNIQUE                  = new Message0(
            DMNMessageType.DRGELEM_NOT_UNIQUE,
            "DRGElement = new Message0(DMNMessageTypeId.FEEL_PROBLEM, BKM | Decision | InputData | KnowledgeSource) name not unique in the model" );
    public static final Message0 DTABLE_MULTIPLEOUT_NAME             = new Message0( DMNMessageType.DTABLE_MULTIPLEOUT_NAME, "Decision table with multiple output should have output name" );
    public static final Message0 DTABLE_MULTIPLEOUT_TYPEREF          = new Message0( DMNMessageType.DTABLE_MULTIPLEOUT_TYPEREF, "Decision table with multiple output should have output typeRef" );
    public static final Message0 DTABLE_PRIORITY_MISSING_OUTVALS     = new Message0( DMNMessageType.DTABLE_PRIORITY_MISSING_OUTVALS, "Decision table with Priority as hit policy requires output to specify output values" );
    public static final Message0 DTABLE_SINGLEOUT_NONAME             = new Message0( DMNMessageType.DTABLE_SINGLEOUT_NONAME, "Decision table with single output should not have output name" );
    public static final Message0 DTABLE_SINGLEOUT_NOTYPEREF          = new Message0( DMNMessageType.DTABLE_SINGLEOUT_NOTYPEREF, "Decision table with single output should not have output typeRef" );
    public static final Message0 ELEMREF_MISSING_TARGET              = new Message0( DMNMessageType.ELEMREF_MISSING_TARGET, "Element reference is pointing to a unknown target" );
    public static final Message0 ELEMREF_NOHASH                      = new Message0(
            DMNMessageType.ELEMREF_NOHASH,
            "This element 'href' reference is expected to be using an anchor  = new Message0(DMNMessageTypeId.FEEL_PROBLEM, hash sign) for pointing to a target element reference" );
    public static final Message0 FAILED_VALIDATOR                    = new Message0( DMNMessageType.FAILED_VALIDATOR, "The Validator Was unable to compile embedded DMN validation rules, validation of the DMN Model cannot be performed." );
    public static final Message1 FAILED_XML_VALIDATION               = new Message1( DMNMessageType.FAILED_XML_VALIDATION, "Failed XML validation of DMN file: %s" );
    public static final Message0 FORMAL_PARAM_DUPLICATED             = new Message0( DMNMessageType.FORMAL_PARAM_DUPLICATED, "formal parameter with duplicated name" );
    public static final Message0 INVOCATION_INCONSISTENT_PARAM_NAMES = new Message0( DMNMessageType.INVOCATION_INCONSISTENT_PARAM_NAMES, "Invocation Binding parameter names SHALL be a subset of the formalParameters of the calledFunction" );
    public static final Message0 INVOCATION_MISSING_TARGET           = new Message0( DMNMessageType.INVOCATION_MISSING_TARGET, "Invocation referencing a DRGElement target not found" );
    public static final Message0 INVOCATION_WRONG_PARAM_COUNT        = new Message0( DMNMessageType.INVOCATION_WRONG_PARAM_COUNT, "Invocation referecing a DRGElement but number of parameters is not consistent with target" );
    public static final Message0 ITEMCOMP_DUPLICATED                 = new Message0( DMNMessageType.ITEMCOMP_DUPLICATED, "itemComponent with duplicated name within a same parent itemDefinition" );
    public static final Message0 ITEMDEF_NOT_UNIQUE                  = new Message0( DMNMessageType.ITEMDEF_NOT_UNIQUE, "itemDefinition name is not unique in the model" );
    public static final Message0 RELATION_DUP_COLUMN                 = new Message0( DMNMessageType.RELATION_DUP_COLUMN, "Relation contains duplicate column name" );
    public static final Message0 RELATION_ROW_CELL_NOTLITERAL        = new Message0( DMNMessageType.RELATION_ROW_CELL_NOTLITERAL, "Relation contains a row with a cell which is not a literalExpression" );
    public static final Message0 RELATION_ROW_CELLCOUNTMISMATCH      = new Message0( DMNMessageType.RELATION_ROW_CELLCOUNTMISMATCH, "Relation contains a row with wrong number of cells" );
    public static final Message0 REQAUTH_NOT_KNOWLEDGESOURCE         = new Message0( DMNMessageType.REQAUTH_NOT_KNOWLEDGESOURCE, "RequiredAuthority is not pointing to a KnowledgeSource" );
    public static final Message0 TYPEREF_NO_FEEL_TYPE                = new Message0( DMNMessageType.TYPEREF_NO_FEEL_TYPE, "This element indicates a 'typeRef' which is not a valid built-in FEEL type" );

    public static interface Message {
        String getMask();

        DMNMessageType getType();
    }
    public abstract static class AbstractMessage
            implements Message {
        private final String         mask;
        private final DMNMessageType type;

        public AbstractMessage(DMNMessageType type, String mask) {
            this.type = type;
            this.mask = mask;
        }

        public String getMask() {
            return this.mask;
        }

        public DMNMessageType getType() {
            return type;
        }
    }
    public static class Message0
            extends AbstractMessage {
        public Message0(DMNMessageType id, String mask) {
            super( id, mask );
        }
    }
    public static class Message1
            extends AbstractMessage {
        public Message1(DMNMessageType id, String mask) {
            super( id, mask );
        }
    }
    public static class Message2
            extends AbstractMessage {
        public Message2(DMNMessageType id, String mask) {
            super( id, mask );
        }
    }
    public static class Message3
            extends AbstractMessage {
        public Message3(DMNMessageType id, String mask) {
            super( id, mask );
        }
    }
    public static class Message4
            extends AbstractMessage {
        public Message4(DMNMessageType id, String mask) {
            super( id, mask );
        }
    }
}
