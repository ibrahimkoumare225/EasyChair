package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Role;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.service.FileStorageService;
import fr.upsaclay.easychair.service.SubmissionService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions/{submissionId}/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileStorageService storageService;
    private final FileStorageService fileStorageService;
    private final UserService userService;
    private final SubmissionService submissionService;


    @GetMapping("/listFiles")
    public String listFiles(@PathVariable Long submissionId,Model model, Authentication authentication){
        List<String> fileNames = storageService.listFiles(submissionId.toString());
        model.addAttribute("files", fileNames);
        model.addAttribute("submissionId", submissionId);
        return "dynamic/submission/files/filesTest";
    }



    //Telechargement
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadfile(@PathVariable Long submissionId,
                                                 @PathVariable String filename,
                                                 Authentication authentication) {
        Optional<User> user= userService.findByEmail(authentication.getName());
        if (user.isEmpty()){
            logger.warn("User connected not found");
        }
        Optional<Submission> submission = submissionService.findOne(submissionId);
        if (submission.isEmpty()){
            logger.warn("Submission {} not found", submissionId);
        }
        logger.debug("Matching user {} with submission {}", authentication.getName(), submissionId);
        Optional<Role> matchedAuthor = user.get().getRoles().stream()
                .filter(role -> role.getId().equals(submission.get().getAuthors().get(0).getId())
                        && role.getConference().getId().equals(submission.get().getConference().getId()))
                .findFirst();
        Optional<Role> matchedRole = user.get().getRoles().stream()
                .filter(role -> role.getConference().getId().equals(submission.get().getConference().getId()) &&
                        (role.getRoleType().equals(RoleType.REVIEWER) ||
                        (role.getRoleType().equals(RoleType.ORGANIZER))))
                .findFirst();
        if (matchedAuthor.isEmpty() && matchedRole.isEmpty()) {
            logger.warn("No match with  user {} of submission{} with any conveniant roles", authentication.getName(), submissionId);
        }
        Resource resource = storageService.load(submissionId.toString(), filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachement; filename=\""+resource.getFilename() + "\"")
                .body(resource);
    }

    //Suppression
    @PostMapping("/{filename:.+}/delete")
    public String deleteFile(@PathVariable Long submissionId,@PathVariable String filename,
                             Authentication authentication, RedirectAttributes redirectAttributes) {
        logger.debug("Trying deleting file {}", filename);
        Optional<User> user= userService.findByEmail(authentication.getName());
        if (user.isEmpty()){
            logger.warn("User connected not found");
            redirectAttributes.addFlashAttribute("message", "User not found");
        }
        Optional<Submission> submission = submissionService.findOne(submissionId);
        if (submission.isEmpty()){
            logger.warn("Submission {} not found", submissionId);
            redirectAttributes.addFlashAttribute("message", "Submission not found");
        }
        logger.debug("Matching user {} with submission {}", authentication.getName(), submissionId);
        Optional<Role> matchedAuthor = user.get().getRoles().stream()
                .filter(role -> role.getId().equals(submission.get().getAuthors().get(0).getId())
                        && role.getConference().getId().equals(submission.get().getConference().getId()))
                .findFirst();
        if (matchedAuthor.isEmpty()) {
            logger.warn("No match with  user {} as Author of submission{}", authentication.getName(), submissionId);
            return "redirect:/conference";
        }
        boolean deleted = storageService.delete(submissionId.toString(), filename);
        redirectAttributes.addFlashAttribute("message", deleted ?
                "File deleted" : "Error during deletion");
        logger.debug("  file {} deleted", filename);
        return "redirect:/conference";
    }

    //Upload
    @PostMapping("/upload")
    public String UploadFile (@PathVariable Long submissionId,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes,Authentication authentication) {
        logger.debug("Trying to upload file {}", file.getOriginalFilename());
        Optional<User> user= userService.findByEmail(authentication.getName());
        if (user.isEmpty()){
            logger.warn("User connected not found");
            redirectAttributes.addFlashAttribute("message", "User not found");
        }
        Optional<Submission> submission = submissionService.findOne(submissionId);
        if (submission.isEmpty()){
            logger.warn("Submission {} not found", submissionId);
            redirectAttributes.addFlashAttribute("message", "Submission not found");
        }
        logger.debug("Matching user {} with submission {}", authentication.getName(), submissionId);
        Optional<Role> matchedAuthor = user.get().getRoles().stream()
                .filter(role -> role.getId().equals(submission.get().getAuthors().get(0).getId())
                        && role.getConference().getId().equals(submission.get().getConference().getId()))
                .findFirst();
        if (matchedAuthor.isEmpty()) {
            logger.warn("No match with  user {} as Author of submission{}", authentication.getName(), submissionId);
            return "redirect:/conference";
        }
        String path=storageService.save(submissionId.toString(), file);
        logger.debug("File successfully uploaded at {}",path);
        redirectAttributes.addFlashAttribute("message", "File uploaded");
        return "redirect:/conference";

    }




}
