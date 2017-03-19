package org.kie.dmn.api.core;

public enum DMNMessageType {
    UNSUPPORTED_ELEMENT( "The referenced element is not supported by the implementation", Tag.COMPILATION, Tag.DMN_CORE),
    REQ_NOT_FOUND( "The referenced node was not found", Tag.COMPILATION, Tag.DMN_CORE),
    TYPE_REF_NOT_FOUND( "The listed type reference could not be resolved", Tag.COMPILATION, Tag.DMN_CORE),
    TYPE_DEF_NOT_FOUND( "The listed type definition was not found", Tag.COMPILATION, Tag.DMN_CORE),
    INVALID_NAME( "The listed name is not a valid FEEL identifier", Tag.COMPILATION, Tag.DMN_CORE),
    INVALID_SYNTAX( "Invalid FEEL syntax on the referenced expression", Tag.COMPILATION, Tag.RUNTIME, Tag.DMN_CORE),
    MISSING_EXPRESSION( "No decision logic was defined for the node or variable", Tag.COMPILATION, Tag.VALIDATION, Tag.DMN_CORE, Tag.DMN_VALIDATOR),
    MISSING_VARIABLE( "A variable declaration is missing", Tag.COMPILATION, Tag.VALIDATION, Tag.DMN_CORE, Tag.DMN_VALIDATOR ),
    VARIABLE_NAME_MISMATCH( "A variable name does not match the node it belongs to", Tag.COMPILATION, Tag.VALIDATION, Tag.DMN_CORE, Tag.DMN_VALIDATOR ),
    DUPLICATE_CONTEXT_ENTRY( "Two or more context entries have variables with the same name", Tag.VALIDATION, Tag.DMN_VALIDATOR ),
    MISSING_TYPE_REF("Type ref not defined", Tag.COMPILATION, Tag.VALIDATION, Tag.DMN_CORE, Tag.DMN_VALIDATOR ),

    FAILED_VALIDATOR ( "The DMN validator failed to load the validation rules. Impossible to proceed with validation.", Tag.VALIDATION, Tag.DMN_VALIDATOR ),
    FAILED_XML_VALIDATION ( "DMN model failed XML schema validation", Tag.VALIDATION, Tag.DMN_VALIDATOR ),


    DECISION_NOT_FOUND( "", Tag.DMN_CORE),                           // API-call related
    MISSING_DEP( "", Tag.DMN_CORE),                                  // compilation
    ERROR_EVAL_NODE( "", Tag.DMN_CORE),                              // runtime
    EXPR_TYPE_NOT_SUPPORTED_IN_NODE( "", Tag.DMN_CORE),              // compilation
    ERR_COMPILING_FEEL( "", Tag.DMN_CORE),                           // runtime
    ERR_EVAL_CTX( "", Tag.DMN_CORE),                                 // runtime
    FEEL_PROBLEM( "", Tag.DMN_CORE),                                 // runtime
    ERR_EVAL( "", Tag.DMN_CORE),                                     // runtime
    ERR_INVOKE( "", Tag.DMN_CORE),                                   // runtime



    DRGELEM_NOT_UNIQUE ( "", Tag.DMN_VALIDATOR ),
    DTABLE_MULTIPLEOUT_NAME ( "", Tag.DMN_VALIDATOR ),
    DTABLE_MULTIPLEOUT_TYPEREF ( "", Tag.DMN_VALIDATOR ),
    DTABLE_PRIORITY_MISSING_OUTVALS ( "", Tag.DMN_VALIDATOR ),
    DTABLE_SINGLEOUT_NONAME ( "", Tag.DMN_VALIDATOR ),
    DTABLE_SINGLEOUT_NOTYPEREF ( "", Tag.DMN_VALIDATOR ),
    ELEMREF_MISSING_TARGET ( "", Tag.DMN_VALIDATOR ),
    ELEMREF_NOHASH ( "", Tag.DMN_VALIDATOR ),
    FORMAL_PARAM_DUPLICATED ( "", Tag.DMN_VALIDATOR ),
    INVOCATION_INCONSISTENT_PARAM_NAMES( "", Tag.DMN_VALIDATOR ),
    INVOCATION_MISSING_TARGET( "", Tag.DMN_VALIDATOR ),
    INVOCATION_WRONG_PARAM_COUNT( "", Tag.DMN_VALIDATOR ),
    ITEMCOMP_DUPLICATED ( "", Tag.DMN_VALIDATOR ),
    ITEMDEF_NOT_UNIQUE ( "", Tag.DMN_VALIDATOR ),
    RELATION_DUP_COLUMN ( "", Tag.DMN_VALIDATOR ),
    RELATION_ROW_CELL_NOTLITERAL ( "", Tag.DMN_VALIDATOR ),
    RELATION_ROW_CELLCOUNTMISMATCH ( "", Tag.DMN_VALIDATOR ),
    REQAUTH_NOT_KNOWLEDGESOURCE ( "", Tag.DMN_VALIDATOR ),
    TYPEREF_NO_FEEL_TYPE( "", Tag.DMN_VALIDATOR ),                   // DUPS of TYPE_REF_NOT_FOUND ?
    ;
    
    private final Tag[] tags;
    private final String description;

    DMNMessageType(String description, Tag... tags) {
        this.description = description;
        this.tags = tags;
    }

    public String getDescription() {
        return this.description;
    }

    public Tag[] getTags() {
        return this.tags;
    }

    public enum Tag {
        // message source
        DMN_CORE, DMN_VALIDATOR,
        // validation phase
        VALIDATION, COMPILATION, RUNTIME
    }

}
