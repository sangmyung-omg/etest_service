package com.tmax.eTest.Contents.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @PropertySource("classpath:application.properties")
@RestController
public class FileController {

  @Value("${etest.contents.file.defaultPath}")
  private String DEFAULT_PATH;

  @GetMapping("/file")
  public ResponseEntity<Object> download() {
    throw new IllegalArgumentException("Filename is null");
  }

  @GetMapping("/file/{filename}")
  public ResponseEntity<Object> download(@PathVariable("filename") String filename, HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    log.info("File Download: " + filename);
    String filePath = DEFAULT_PATH + filename;
    File downloadFile = new File(filePath);
    FileInputStream inputStream = new FileInputStream(downloadFile);

    int fileSize = (int) downloadFile.length();
    log.info("File Size: " + fileSize);

    String mimeType = request.getServletContext().getMimeType(filePath);
    if (CommonUtils.stringNullCheck(mimeType))
      mimeType = "application/octet-stream";
    log.info("MimeType: " + mimeType);

    // Path path = Paths.get(downloadFile.getAbsolutePath());
    // ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    // return ResponseEntity.ok().header("Content-Disposition", "attachment;
    // filename=" + filename)
    // .contentLength(downloadFile.length()).contentType(MediaType.parseMediaType(mimeType)).body(resource);

    response.setContentType(mimeType);
    response.setContentLength(fileSize);
    response.setHeader("Content-Disposition", "attachment; filename=" + filename);

    // start download stream
    StreamUtils.copy(inputStream, response.getOutputStream());

    response.flushBuffer();
    inputStream.close();
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
