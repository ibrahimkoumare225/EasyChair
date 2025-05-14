package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions")
public class FileController {

    private final FileStorageService storageService;
    private final FileStorageService fileStorageService;


    @GetMapping("/{submissionId}/files/readonly")
    public String listFilesReadonly(@PathVariable Long submissionId,Model model, Authentication authentication){
        List<String> fileNames = storageService.listFiles(submissionId.toString());
        model.addAttribute("files", fileNames);
        model.addAttribute("submissionId", submissionId);
        return "dynamic/submission/filesReadOnly :: fileList";
    }

    @GetMapping("/{submissionId}/files/edit")
    public String listFilesForModification(@PathVariable Long submissionId,Model model, Authentication authentication){
        List<String> fileNames = storageService.listFiles(submissionId.toString());
        model.addAttribute("files", fileNames);
        model.addAttribute("submissionId", submissionId);
        return "dynamic/submission/filesEdit :: fileList";
    }

    //Telechargement
    @GetMapping("/{submissionId}/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadfile(@PathVariable Long submissionId,
                                                 @PathVariable String filename,
                                                 Authentication authentication) {
        Resource resource = storageService.load(submissionId.toString(), filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachement; filename=\""+resource.getFilename() + "\"")
                .body(resource);
    }

    //Suppression
    @PostMapping("/{submissionId}/files/{filename}/delete")
    public String deleteFile(@PathVariable Long submissionId,@PathVariable String filename,
                             Authentication authentication, RedirectAttributes redirectAttributes) {
        boolean deleted = storageService.delete(submissionId.toString(), filename);
        redirectAttributes.addFlashAttribute("message", deleted ?
                "File deleted" : "Error during deletion");
        return "redirect:/conference";
    }

    //Upload
    @PostMapping("{submissionId}/files/{filename}/upload")
    public String UploadFile (@PathVariable Long submissionId,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes){
        storageService.save(submissionId.toString(), file);
        redirectAttributes.addFlashAttribute("message", "File uploaded");
        return "redirect:/conference";

    }




}
