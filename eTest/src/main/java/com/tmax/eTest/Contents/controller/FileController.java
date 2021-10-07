package com.tmax.eTest.Contents.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileController {

  @Value("${file.path}")
  private String DEFAULT_PATH;

  @Autowired
  private CommonUtils commonUtils;

  private static final String CONTETNS_PATH = "contents" + File.separator;

  @GetMapping("/file/**")
  public void download(HttpServletRequest request, HttpServletResponse response) {
    String filename = request.getRequestURI().split(request.getContextPath() + "/file/")[1];
    String filePath = DEFAULT_PATH + CONTETNS_PATH + filename;
    log.info("File Download: " + filePath);

    File downloadFile = new File(filePath);
    if (!downloadFile.exists())
      throw new ContentsException(ErrorCode.FILE_ERROR, filePath);

    int fileSize = (int) downloadFile.length();
    log.info("File Size: " + fileSize);

    String mimeType = request.getServletContext().getMimeType(filePath);
    if (commonUtils.stringNullCheck(mimeType))
      mimeType = "application/octet-stream";
    log.info("MimeType: " + mimeType);

    response.setContentType(mimeType);
    response.setContentLength(fileSize);
    // response.setHeader("Content-Disposition", "attachment; filename=" +
    // filename);

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(downloadFile);
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    } catch (IOException e) {
      throw new ContentsException(ErrorCode.FILE_ERROR, filePath);
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        log.error("File InputStream Close Error.");
      }
    }

    // return new ResponseEntity<>(null, HttpStatus.OK);

    // System.out.println("Executable: " + file.canExecute());
    // System.out.println("Readable: " + file.canRead());
    // System.out.println("Writable: " + file.canWrite());

    // Path path = Paths.get(downloadFile.getAbsolutePath());
    // ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    // return
    // ResponseEntity.ok().contentLength(downloadFile.length()).contentType(MediaType.parseMediaType(mimeType))
    // .body(resource);
  }
}
