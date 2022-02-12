package run.ergou.javawebsec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/file")
public class Upload {
    private static final String UPLOADED_FOLDER = "E:\\tmp\\";

    @GetMapping("")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "You successfully uploaded '" + UPLOADED_FOLDER + file.getOriginalFilename() + "'";

    }
}
