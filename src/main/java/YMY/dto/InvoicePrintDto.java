package YMY.dto;

import YMY.entities.Cities;
import YMY.entities.Invoice;
import YMY.entities.Works;
import YMY.repositories.CitiesRepository;
import YMY.repositories.InvoiceRepository;
import YMY.utils.Check;
import YMY.utils.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InvoicePrintDto {

    final InvoiceRepository invoiceRepository;
    final CitiesRepository citiesRepository;
    public InvoicePrintDto(InvoiceRepository invoiceRepository, CitiesRepository citiesRepository) {
        this.invoiceRepository = invoiceRepository;
        this.citiesRepository = citiesRepository;
    }

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    //Create header line
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Fatura");
    }

    //Create cell for excel file
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(Invoice invoice){
        int rowCount = 14; //Excel'de 15. satırı temsil eder. Yapılan işler buradan itibaren konumlandırılır.
        List<Works> worksList = invoice.getWorkses();
        Cities cities = citiesRepository.findByCityKey(Integer.parseInt(invoice.getCompany().getCity())).get();
        float result = 0;
        float discount = 0;
        float kdv = 0;

        float iskontoTutarı = 0;
        float kdvTutarı = 0;

        //Create row
        Row row2 = sheet.createRow(6);
        Row row3 = sheet.createRow(7);
        Row row4 = sheet.createRow(8);
        Row row7 = sheet.createRow(10);
        Row row9 = sheet.createRow(12);
        Row row10 = sheet.createRow(13);
        Row row11 = sheet.createRow(14);
        Row row33 = sheet.createRow(36);
        Row row34 = sheet.createRow(37);
        Row row36 = sheet.createRow(39);
        Row row37 = sheet.createRow(40);
        Row row39 = sheet.createRow(42);

        //Normal Yazı
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        font.setFontName("Arial");
        style.setFont(font);

        //Başlık Yazı
        CellStyle style2 = workbook.createCellStyle();
        XSSFFont font2 = workbook.createFont();
        font2.setFontHeight(8);
        font2.setFontName("Arial");
        font2.setBold(true);
        style2.setFont(font2);

        //Tutar Bilgileri font ayarı - Sağa Yaslı
        CellStyle style3 = workbook.createCellStyle();
        XSSFFont font3 = workbook.createFont();
        font3.setFontHeight(8);
        font3.setFontName("Arial");
        style3.setFont(font3);
        style3.setAlignment(HorizontalAlignment.RIGHT);

        //Tutar Bilgileri font ayarı - Ortalı
        CellStyle style4 = workbook.createCellStyle();
        XSSFFont font4 = workbook.createFont();
        font4.setFontHeight(8);
        font4.setFontName("Arial");
        style4.setFont(font4);
        style4.setAlignment(HorizontalAlignment.CENTER);

        createCell(row2,0,"                    " + invoice.getCompany().getName(),style); //Şirket isminin yazdırılması
        createCell(row3,0,"                    " + invoice.getCompany().getAddress(),style); //Şirket adresinin yazdırılması
        createCell(row4,0,"                    " + invoice.getCustomer().getPlate() + " " + invoice.getCustomer().getBrand() + " " + invoice.getCustomer().getModel(),style); // Araç plaka, marka, model yazdırılması
        createCell(row7,0,invoice.getCompany().getTown() + "/" + cities.getName(),style3); //Şirket ilçe/il yazdırılması
        createCell(row9,0,"Müş.V.D        :  " + invoice.getCompany().getTaxOffice(),style); // Müş.V.D başlığı
        //createCell(row9,1,invoice.getCompany().getTaxOffice(),style); // Müş.V.D değeri
        createCell(row9,4,"Düz. Tarih          :",style2); // Düz Tarih başlığı
        createCell(row10,4,"İrs. Tarih            :",style2); // İrs Tarih başlığı
        createCell(row11,0,"Vergi/Tc.No   :  " + invoice.getCompany().getTaxNumber(),style); // Vergi/Tc.No: başlığı
        //createCell(row11,1,invoice.getCompany().getTaxNumber(),style); // Vergi/Tc.No: değeri
        createCell(row11,4,"İrs. No                 :",style2); // İrs No: başlığı

        for (Works works : worksList) {
            Row row = sheet.createRow(rowCount++);

            createCell(row, 0, works.getWork(), style);
            createCell(row, 3, works.getQuantity(), style4);
            createCell(row, 4, String.valueOf(works.getUnitPrice()), style4);
            createCell(row, 5, String.valueOf(works.getTotal()), style3);

            result += works.getTotal();

        }

        //İskonto ve KDV değerlerinin hesaplanması
        discount = invoice.getDiscount();
        kdv = invoice.getVat();
        if(kdv == 18 && discount > 0){
            iskontoTutarı = result*(discount/100);
            kdvTutarı = (result - iskontoTutarı) * (kdv/100);
            System.out.println("KDV ve iskonto var: ");
        }
        else if(kdv == 18 && discount <= 0){
            iskontoTutarı = result*(discount/100);
            kdvTutarı = (result - iskontoTutarı) * (kdv/100);
            System.out.println("KDV var ama iskonto yok: ");
        }
        else if(kdv == 0 && discount > 0){
            iskontoTutarı = result*(discount/100);
            kdvTutarı = (result - iskontoTutarı) * (kdv/100);
            System.out.println("KDV yok ama iskonto var: " );
        }

        createCell(row33,0,"KDV MT",style); //KDV MT başlığı
        createCell(row33,1,"KDV",style); //KDV başlığı
        createCell(row33,2,"KDV TUT",style3); //KDV TUT başlığı
        createCell(row33,3,"İSKONTO",style3); //İskonto başlığı
        createCell(row33,4,"TUTAR",style); //Tutar başlığı
        createCell(row33,5,result + " TL",style3); // KDV'siz ve iskontosuz toplam değer
        createCell(row34,0,String.valueOf(result),style); //KDV MT değeri
        createCell(row34,1,String.valueOf(invoice.getVat()),style); //KDV değeri
        createCell(row34,2,String.valueOf(kdvTutarı),style3); //KDV TUT değeri
        createCell(row34,3,"      " + invoice.getDiscount(),style); //İskonto değeri
        createCell(row34,4,"İSKONTO TUT",style); //İSKONTO    TUT başlığı
        createCell(row34,5,iskontoTutarı + " TL",style3); //İSKONTO    TUT değeri
        createCell(row36,4,"TOP  KDV  MAT",style); //TOP  KDV  MAT başlığı
        createCell(row36,5,(result-iskontoTutarı) + " TL",style3); //TOP  KDV  MAT değeri
        createCell(row37,4,"TOPLAM     KDV",style); //TOPLAM     KDV başlığı
        createCell(row37,5,kdvTutarı + " TL",style3); //TOPLAM     KDV değeri
        createCell(row39,4,"FATURA TOPLAMI",style); //FATURA TOPLAMI başlığı
        createCell(row39,5,invoice.getDebt() + " TL",style3); //FATURA TOPLAMI değeri


    }

    public void export(HttpServletResponse response,Invoice invoice) throws IOException {
        writeHeaderLine();
        writeDataLines(invoice);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }

    //Export Excel
    public Map<Check,Object> exportToExcel(String stId,HttpServletResponse response){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
            if(optionalInvoice.isPresent()){
                Invoice invoice = optionalInvoice.get();
                response.setContentType("application/json");
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=Fatura" + ".xlsx";
                response.setHeader(headerKey, headerValue);
                export(response,invoice);
                hm.put(Check.status,true);
                hm.put(Check.message,"Fatura başarılı bir şekilde oluşturuldu!");
            }
        } catch (Exception e) {
            String error = "Excel dışarı aktarılırken bir sorun oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }
    /*public void exportToExcel(String stId,HttpServletResponse response){

        try {
            int id = Integer.parseInt(stId);
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
            if(optionalInvoice.isPresent()){
                Invoice invoice = optionalInvoice.get();
                response.setContentType("application/octet-stream");
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String currentDateTime = dateFormatter.format(new Date());

                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
                response.setHeader(headerKey, headerValue);
                export(response,invoice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}
