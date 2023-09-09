package data;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class data_driven {

    public Object[][] read_data() throws IOException, InvalidFormatException {
        File file =new File("C:\\Users\\MohamedR37\\Downloads\\data.xlsx");
        XSSFWorkbook  wb=new XSSFWorkbook(file);
        XSSFSheet sheet=wb.getSheet("sheet1");

        int rowcount = sheet.getPhysicalNumberOfRows();
        int colnum= sheet.getRow(0).getLastCellNum();
        String [][] array=new String[rowcount-1][colnum];
        for (int i=1;i<rowcount;i++){
            for (int j=0;j<colnum;j++){
                XSSFRow ro=sheet.getRow(i);
                array[i-1][j]=ro.getCell(j).getStringCellValue();
            }
        }
        return array;
    }
}
