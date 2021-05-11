package lk.icoder.sftpspringboot.controller;

import lk.icoder.sftpspringboot.service.FileTransferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project sftp-springboot
 * @Author DILAN on 5/6/2021
 */
@RestController
public class FileTransferController {

   private final FileTransferService fileTransferService;

   public FileTransferController(FileTransferService fileTransferService) {
      this.fileTransferService = fileTransferService;
   }

   @GetMapping("test")
   public String executeTransfer() {
      fileTransferService.fileTransfer();
      return "transferred.";
   }
}
