package com.stylefeng.guns.rest.common.util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author junyong.chen
 * @date 2019/4/11 17:19
 * @description
 */
@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtil {
    private String hostName;
    private int port;
    private String userName;
    private String password;
    private FTPClient ftpClient;

    /**
     *  初始化ftp
     */
    private void initFTPClient() {
        ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName, port);
            ftpClient.login(userName, password);
        } catch (Exception e) {
            log.error("初始化FTP失败", e);
        }
    }

    /**
     *  将路径中的文件转为字符串
     * @param fileAddress
     * @return
     */
    public String getFileStrByAddress(String fileAddress) {
        initFTPClient();
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(fileAddress)));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
            ftpClient.logout();
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("关闭BufferedReader流失败", e);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        FTPUtil ftpUtil = new FTPUtil();
        String fileStrByAddress = ftpUtil.getFileStrByAddress("seats/cgs.json");
        System.out.println(fileStrByAddress);
    }
}
