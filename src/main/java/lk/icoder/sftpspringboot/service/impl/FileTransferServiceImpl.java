package lk.icoder.sftpspringboot.service.impl;

import com.jcraft.jsch.*;
import lk.icoder.sftpspringboot.config.JSchConfig;
import lk.icoder.sftpspringboot.service.FileTransferService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Project sftp-springboot
 * @Author DILAN on 5/5/2021
 */
@Service
public class FileTransferServiceImpl implements FileTransferService {

    private static final String REMOTE_HOST = "localhost";
    private static final String USERNAME = "sftpuser";
    private static final String PASSWORD = "123";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    @Value("${downloadedLocal}")
    private Resource[] resources;

    private JSchConfig jSchConfig;

    public FileTransferServiceImpl(JSchConfig jSchConfig) {
        this.jSchConfig = jSchConfig;
    }

    @Override
    public void fileTransfer() {

        // remote server
        String finalRemoteLocation = "\\secure_exchange\\";
        String baseRemoteFile = "/cardcenter/";

        // local
        String downloadedLocal = "F:\\MY WORK\\FTP\\local_file_path\\";

        Session jschSession = null;

        try {

            // get files from remote and store it in local
            Vector<ChannelSftp.LsEntry> fileList = jSchConfig.createChannelSftp().ls("\\cardcenter\\");
            for (int i = 0; i < fileList.size(); i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) fileList.get(i);
                if (!((lsEntry.getFilename().equals(".")) || (lsEntry.getFilename().equals("..")))) {
                    jSchConfig.createChannelSftp().get(baseRemoteFile + lsEntry.getFilename(), downloadedLocal + lsEntry.getFilename());
                }

            }

            // transfer file from local to remote server
            Set<String> fileSet = Stream.of(new File(downloadedLocal).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toSet());

            Iterator<String> iterator = fileSet.iterator();
            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
                jSchConfig.createChannelSftp().put(downloadedLocal + iterator.next(), finalRemoteLocation);
            }



            jSchConfig.createChannelSftp().exit();

        } catch (JSchException | SftpException e) {

            e.printStackTrace();

        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

    }

//   file list
//   private void filePath() {
//      String fullLocation = "upload/";
//      channelSftp.cd(fullLocation);
//      Vector<ChannelSftp.LsEntry> lsFiles = channelSftp.ls("/" + fullLocation);
//      if (lsFiles != null && !lsFiles.isEmpty()) {
//         for (ChannelSftp.LsEntry files : lsFiles) {
//            if (files != null) {
//               String fileNameToget = files.getFilename();
//               String filePath = fullLocation + "/" + files.getFilename();
//
//               SftpATTRS attrs = files.getAttrs();
//
//               if (fileNameToget.equals(".") || fileNameToget.equals("..")) {
//                  continue;
//               }
//
//               channelSftp.get("/" + filePath, "/home/anjali" + fileNameToget);
//            }
//         }
//      }
//
//   }
}
