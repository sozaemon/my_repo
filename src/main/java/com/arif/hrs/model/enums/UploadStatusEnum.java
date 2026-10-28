package com.arif.hrs.model.enums;

public enum UploadStatusEnum {
  /**
   * state when record uploaded
   */
  UPLOADED,
  /**
   * state when not valid record is fixed and re-validate
   * and re-process
   */
  REVALIDATE,
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
