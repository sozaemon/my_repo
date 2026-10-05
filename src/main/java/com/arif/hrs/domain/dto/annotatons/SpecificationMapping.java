package com.arif.hrs.domain.dto.annotatons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificationMapping {

  String joinTable() default "";

  String targetColumn() default "";

  enum JoinType {
    LEFT, RIGHT
  }

  JoinType joinType() default JoinType.LEFT;
}
