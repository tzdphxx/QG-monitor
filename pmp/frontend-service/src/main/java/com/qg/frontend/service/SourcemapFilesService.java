package com.qg.frontend.service;


import com.qg.common.domain.po.Result;
import org.springframework.web.multipart.MultipartFile;

public interface SourcemapFilesService {
    Result uploadFile(String projectId, String timestamp, String version, String buildVersion, MultipartFile[] files, String[] jsFilenames, String fileHashes);
}
