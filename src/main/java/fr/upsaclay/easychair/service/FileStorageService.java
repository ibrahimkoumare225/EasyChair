package fr.upsaclay.easychair.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileStorageService {

    void initSubmissionDirectory(String submissionId);

    List<String> listFiles(String submissionId);

    String save(String submisionId, MultipartFile file);

    Resource load(String submisionId, String filename);

    boolean delete(String submisionId, String filename);

}
