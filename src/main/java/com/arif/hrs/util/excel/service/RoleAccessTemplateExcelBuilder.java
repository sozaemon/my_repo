package com.arif.hrs.util.excel.service;

import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.arif.hrs.util.excel.excelmodel.RoleAccessTemplateExcelModel;
import com.arif.hrs.util.excel.excelmodel.RoleExcelModel;

/**
 * Builds the role access upload template. The first sheet follows the layout
 * expected by {@link com.arif.hrs.util.excel.ExcelReader}, the second sheet
 * lists every available role as a reference for the user.
 */
public class RoleAccessTemplateExcelBuilder extends ExcelBuilderXlsxService<RoleAccessTemplateExcelModel> {

  private static final String TEMPLATE_SHEET_NAME = "Role Access";
  private static final String ROLE_SHEET_NAME = "Roles";
  private static final String[] ROLE_HEADERS = { "Code", "Name", "Description" };

  private final List<RoleExcelModel> roles;

  public RoleAccessTemplateExcelBuilder(List<RoleExcelModel> roles) {
    super(List.of(), RoleAccessTemplateExcelModel.class);
    this.roles = roles;
  }

  @Override
  protected Sheet createWorkSheet() {
    return wb.createSheet(TEMPLATE_SHEET_NAME);
  }

  @Override
  public Workbook buildWorkbook() {
    Workbook workbook = super.buildWorkbook();
    createRoleReferenceSheet(workbook);
    return workbook;
  }

  private void createRoleReferenceSheet(Workbook workbook) {
    Sheet roleSheet = workbook.createSheet(ROLE_SHEET_NAME);
    CellStyle headerStyle = createCellStyle(IndexedColors.GREY_25_PERCENT);

    Row headerRow = roleSheet.createRow(0);
    for (int i = 0; i < ROLE_HEADERS.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(ROLE_HEADERS[i]);
      cell.setCellStyle(headerStyle);
    }

    int rowIndex = 1;
    for (RoleExcelModel role : roles) {
      Row row = roleSheet.createRow(rowIndex++);
      row.createCell(0).setCellValue(Objects.toString(role.getCode(), ""));
      row.createCell(1).setCellValue(Objects.toString(role.getName(), ""));
      row.createCell(2).setCellValue(Objects.toString(role.getDescription(), ""));
    }

    for (int i = 0; i < ROLE_HEADERS.length; i++) {
      roleSheet.autoSizeColumn(i);
    }
  }
}
