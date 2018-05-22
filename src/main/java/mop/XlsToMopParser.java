package mop;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;

public class XlsToMopParser {
    private XSSFWorkbook workbook;

    public XlsToMopParser(File file) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace(); // TODO(MG) handle this exception
        }
    }

    public XlsToMopParser(String filestring) {
        new XlsToMopParser(new File(filestring));
    }

    // TODO (MG) rewrite to parse csv file
    public HashSet<MopInfo> parseMops() {
        XSSFSheet sheet = workbook.getSheetAt(0);

        Row row;

        HashSet<MopInfo> res = new HashSet<MopInfo>();
        try {

            for (int i = 6; i < 299; ++i) {
                row = sheet.getRow(i);

                String branch = row.getCell(2).getStringCellValue();
                String locality = row.getCell(3).getStringCellValue();
                String name = row.getCell(4).getStringCellValue();
                double y = row.getCell(5).getNumericCellValue();
                double x = row.getCell(6).getNumericCellValue();
                GeoPosition g = new GeoPosition(x, y);
                String road = row.getCell(8).getStringCellValue();
                String mileageString = row.getCell(9).getStringCellValue();
                mileageString = mileageString.replace('+', '.');
                double mileage;
                try {
                    mileage = Double.parseDouble(mileageString);
                } catch (NumberFormatException e) {
                    mileage = -1.;
                }
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
                        equipmentInfo, mileage));
            }
        } catch (Exception e) {
            return null;
        }
        return res;
    }

    private boolean boolCell(Row row, int i) {
        String content = row.getCell(i).getStringCellValue();
        return content.equals("tak");
    }
}
