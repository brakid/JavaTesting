module com.brakid.runner {
    requires java.base;
    requires com.google.common;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.datatype.guava;
    requires org.slf4j;
    requires lombok;
    
    opens com.brakid.runner.types to tools.jackson.databind;
    opens com.brakid.runner.storage to tools.jackson.databind;
    
    exports com.brakid.runner;
    exports com.brakid.runner.storage;
    exports com.brakid.runner.types;
    exports com.brakid.runner.utils;
}