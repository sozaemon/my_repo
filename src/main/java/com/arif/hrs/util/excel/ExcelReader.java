package com.arif.hrs.util.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

public abstract class ExcelReader<T> {

  public List<T> parseWorkbook(MultipartFile file,
      String[] headers,
      Integer startDataRow,
      Integer sheetIndex) throws IOException {

    List<T> result = new ArrayList<>();
    if (file == null) {
      throw new IllegalArgumentException("file is null");
    }
    String fileName = Objects.requireNonNullElse(String.valueOf(file.getOriginalFilename()), "")
        .toLowerCase(Locale.ROOT);

    if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
      throw new IllegalArgumentException("Only .xlsx and .xls file supported");
    }
    try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {

      if (wb.getNumberOfSheets() < sheetIndex) {
        throw new IllegalArgumentException("Cannot read Sheet data");
      }

      Sheet sheet = wb.getSheetAt(0);
      if (sheet.getPhysicalNumberOfRows() == 0) {
        throw new IllegalArgumentException("Sheet is Empty");
      }

      DataFormatter formatter = new DataFormatter();

      int r = startDataRow;

      while (r < sheet.getLastRowNum()) {
        Row row = sheet.getRow(r);
        Map<String, Object> mappedRow = new HashMap<>();

        for (int h = 1; h < headers.length; h++) {
          if (mappedRow.get(headers[h]) != null) {
            throw new IllegalArgumentException("Duplicated headers");
          }

          Cell cell = row.getCell(h);

          mappedRow.put(headers[h], cell == null ? ""
              : formatter
                  .formatCellValue(cell).trim());

        }
        result.add(mapToResult(mappedRow));
        r++;
      }

    } catch (EncryptedDocumentException ex) {
      throw new IllegalArgumentException("Password protected workbooks are not supported");
    }

    return result;
  }

  protected abstract T mapToResult(Map<String, Object> input);
}
