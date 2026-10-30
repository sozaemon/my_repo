package com.arif.hrs.model.enums;

public enum UploadStatusEnum {
  /**
   * state when record is validated
   * and valid
   */
  VALID,
  /**
   * state when record is validate
   * and not valid
   */
  NOTVALID,
  /**
   * state when record is inserted/updated to actual tables
   */
  COMPLETED
}
