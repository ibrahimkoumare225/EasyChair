package fr.upsaclay.easychair.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileStorageService {

    public void initSubmissionDirectory(String submissionId);

    public List<String> listFiles(String submissionId);

    public String save(String submisionId, MultipartFile file);

    public Resource load (String submisionId, String filename);

    public boolean delete (String submisionId, String filename);

}
