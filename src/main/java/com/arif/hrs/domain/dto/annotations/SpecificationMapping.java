package com.arif.hrs.domain.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificationMapping {

  String joinTable() default "";

  String targetColumn() default "";

  String sortMapping() default "";

  enum JoinType {
    LEFT, RIGHT
  }

  JoinType joinType() default JoinType.LEFT;
}
