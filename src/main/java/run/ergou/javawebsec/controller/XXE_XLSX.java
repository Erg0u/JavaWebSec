package run.ergou.javawebsec.controller;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/xxe-xlsx")
public class XXE_XLSX {

    @GetMapping("")
    public String upload() {
        return "xxe-xlsx";
    }

    /**
     * xlsx-streamer
     * 2.0.0及之前版本
     * xl/workbook.xml
     */
    @PostMapping("/xlsx")
    public void xxeXlsx(MultipartFile file) throws Exception {
        StreamingReader.builder().open(file.getInputStream());
    }

    /**
     * poi-ooxml
     * 3.10及之前版本
     * [Content_Types].xml
     */
    @PostMapping("/xlsx2")
    public void xxeXlsx2(MultipartFile file) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
    }
}
