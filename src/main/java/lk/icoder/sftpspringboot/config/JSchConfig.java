package lk.icoder.sftpspringboot.config;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Project sftp-springboot
 * @Author DILAN on 5/11/2021
 */
@Component
public class JSchConfig {

   private final Logger logger = LoggerFactory.getLogger(JSchConfig.class);

   @Value("${sftp.host}")
   private String host;

   @Value("${sftp.port}")
   private Integer port;

   @Value("${sftp.username}")
   private String username;

   @Value("${sftp.password}")
   private String password;

   @Value("${sftp.sessionTimeout}")
   private Integer sessionTimeout;

   @Value("${sftp.channelTimeout}")
   private Integer channelTimeout;

   public ChannelSftp createChannelSftp() throws JSchException, SftpException{
      try {
         JSch jSch = new JSch();
         Session session = jSch.getSession(username, host, port);
         session.setConfig("StrictHostKeyChecking", "no");
         session.setPassword(password);
         session.connect(sessionTimeout);
         Channel channel = session.openChannel("sftp");
         channel.connect(channelTimeout);
         return (ChannelSftp) channel;
      } catch(JSchException ex) {
         logger.error("Create ChannelSftp error", ex);
      }

      return null;
   }
}
