package mop;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

public class XlsToMopParser {
    private XSSFWorkbook workbook;

    public XlsToMopParser(String filestring) {
        try {
            FileInputStream file = new FileInputStream(getClass().getClassLoader()
                    .getResource(filestring).getFile()); // TODO what to do with nullptr exception
            this.workbook = new XSSFWorkbook(file);
            file.close();
        } catch (FileNotFoundException e) {
            // TODO some reasonable handling
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashSet<MopInfo> parseMops() {
        XSSFSheet sheet = workbook.getSheetAt(0);

        Row row;

        HashSet<MopInfo> res = new HashSet<MopInfo>();

        for (int i = 6; i < 299; ++i) {
            row = sheet.getRow(i);

            String branch = row.getCell(2).getStringCellValue();
            String locality = row.getCell(3).getStringCellValue();
            String name = row.getCell(4).getStringCellValue();
            double y = row.getCell(5).getNumericCellValue();
            double x = row.getCell(6).getNumericCellValue();
            GeoPosition g = new GeoPosition(x, y);
            String road = row.getCell(8).getStringCellValue();
            String direction = row.getCell(10).getStringCellValue();
            int type = 1; // TODO

            MopParkingSpacesInfo parkingSpacesInfo = new MopParkingSpacesInfo(
                    (int) row.getCell(12).getNumericCellValue(),
                    (int) row.getCell(13).getNumericCellValue(),
                    (int) row.getCell(14).getNumericCellValue());

            MopEquipmentInfo equipmentInfo = new MopEquipmentInfo(boolCell(row, 15),
                    boolCell(row, 16), boolCell(row, 17), boolCell(row, 18), boolCell(row, 19),
                    boolCell(row, 20), boolCell(row, 21), boolCell(row, 22), boolCell(row, 23),
                    boolCell(row, 24), boolCell(row, 25));

            res.add(new MopInfo(branch, locality, name, g, road, direction, type, parkingSpacesInfo,
                    equipmentInfo));
        }
        return res;
    }

    private boolean boolCell(Row row, int i) {
        String content = row.getCell(i).getStringCellValue();
        return content.equals("tak");
    }
}
