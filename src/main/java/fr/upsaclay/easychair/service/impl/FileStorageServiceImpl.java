package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.config.FileStorageConfig;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.service.FileStorageService;
import fr.upsaclay.easychair.service.SubmissionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation ;
    private final SubmissionService submissionService;

    public FileStorageServiceImpl(FileStorageConfig config, SubmissionService submissionService) {
        fileStorageLocation = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        }catch(IOException ex){
            throw new RuntimeException("Could not create the directory " + fileStorageLocation, ex);
        }
        this.submissionService = submissionService;
    }

    public void initSubmissionDirectory(String submissionId) {
        try {
            Path submissionDir = fileStorageLocation.resolve(submissionId);
            Files.createDirectories(submissionDir);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier pour la soumission " + submissionId, e);
        }
    }

    public List<String> listFiles(String submissionId) {

            Path submissionDir= fileStorageLocation.resolve(submissionId);
            if(!Files.exists(submissionDir)){
                return List.of();
            }
            try (Stream<Path> stream = Files.list(submissionDir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toList());
            }catch (IOException e){
                throw new RuntimeException("Could not list files " + submissionId, e);
        }
    }


    @Override
    public String save(String submissionId,MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            Path submissionDir = fileStorageLocation.resolve(submissionId);
            if (!Files.exists(submissionDir)) {
                Files.createDirectories(submissionDir);
            }

            Path targetLocation = submissionDir.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch(IOException ex){
            throw new RuntimeException("Could not save file " + fileName, ex);
        }
    }

    @Override
    public Resource load(String submissionId,String filename) {
        try{

            Path filePath = fileStorageLocation.resolve(submissionId).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new RuntimeException("Could not read file " + filePath);

        }catch (MalformedURLException e){
            throw new RuntimeException("Could not load file " + filename, e);
        }
    }

    @Override
    public boolean delete(String submissionId,String filename) {
        try{
            Path filePath = fileStorageLocation.resolve(submissionId).resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        }catch(IOException e){
            throw new RuntimeException("Could not delete file " + filename, e);
        }


    }
}
