package com.tmax.eTest.Contents.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
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

  // @GetMapping("/file")
  // public ResponseEntity<Object> download(@RequestParam(value = "filename",
  // required = true) String filename,
  // HttpServletRequest request, HttpServletResponse response) throws IOException
  // {

  @GetMapping("/file/**")
  public ResponseEntity<Object> download(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String filename = request.getRequestURI().split(request.getContextPath() + "/file/")[1];

    log.info("File Download: " + filename);
    String filePath = DEFAULT_PATH + CONTETNS_PATH + filename;
    File downloadFile = new File(filePath);
    FileInputStream inputStream = new FileInputStream(downloadFile);

    int fileSize = (int) downloadFile.length();
    log.info("File Size: " + fileSize);

    String mimeType = request.getServletContext().getMimeType(filePath);
    if (commonUtils.stringNullCheck(mimeType))
      mimeType = "application/octet-stream";
    log.info("MimeType: " + mimeType);

    // System.out.println("Executable: " + file.canExecute());
    // System.out.println("Readable: " + file.canRead());
    // System.out.println("Writable: " + file.canWrite());

    // Path path = Paths.get(downloadFile.getAbsolutePath());
    // ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    // return
    // ResponseEntity.ok().contentLength(downloadFile.length()).contentType(MediaType.parseMediaType(mimeType))
    // .body(resource);

    response.setContentType(mimeType);
    response.setContentLength(fileSize);
    // response.setHeader("Content-Disposition", "attachment; filename=" +
    // filename);

    // start download stream
    StreamUtils.copy(inputStream, response.getOutputStream());

    response.flushBuffer();
    inputStream.close();
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
