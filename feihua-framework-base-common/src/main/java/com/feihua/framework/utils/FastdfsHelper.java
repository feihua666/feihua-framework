package com.feihua.framework.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by yangwei
 * Created at 2018/9/25 15:14
 */
public class FastdfsHelper {

    private static Logger logger = LoggerFactory.getLogger(FastdfsHelper.class);

    private int connectTimeout;
    private int networkTimeout;
    private String charset;
    private int tracker_http_port;
    private boolean anti_steal_token;
    private String secret_key;
    private String tracker_server;
    private String tracker_ngnix_addr;

    public void init() throws FileNotFoundException, IOException, MyException {


        ClientGlobal.g_connect_timeout = this.connectTimeout;
        if (ClientGlobal.g_connect_timeout < 0) {
            ClientGlobal.g_connect_timeout = ClientGlobal.DEFAULT_CONNECT_TIMEOUT;
        }

        ClientGlobal.g_network_timeout = this.networkTimeout;
        if (ClientGlobal.g_network_timeout < 0) {
            ClientGlobal.g_network_timeout = ClientGlobal.DEFAULT_NETWORK_TIMEOUT;
        }

        ClientGlobal.g_charset = charset;
        if (ClientGlobal.g_charset == null || ClientGlobal.g_charset.length() == 0) {
            ClientGlobal.g_charset = "ISO8859-1";
        }

        String trackerServers = this.tracker_server;
        String[] szTrackerServers = trackerServers.split(";");
        if (szTrackerServers == null) {
            throw new MyException("item \"tracker_server\" in " + "fdfs_client" + " not found");
        } else {
            InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];

            for(int i = 0; i < szTrackerServers.length; ++i) {
                String[] parts = szTrackerServers[i].split("\\:", 2);
                if (parts.length != 2) {
                    throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                }

                tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }

            ClientGlobal.g_tracker_group = new TrackerGroup(tracker_servers);
            ClientGlobal.g_tracker_http_port = tracker_http_port;
            ClientGlobal.g_anti_steal_token = anti_steal_token;
            if (ClientGlobal.g_anti_steal_token) {
                ClientGlobal.g_secret_key = secret_key;
            }

        }
    }

    public String uploadFile(byte[] byteFile, String ext_file) throws IOException, MyException {
        TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
        // 拼接服务区的文件路径
        StringBuffer sbPath = new StringBuffer();
        try {
            String[] strings = null;
            synchronized (storageClient) {
                //利用字节流上传文件
                strings = storageClient.upload_file(byteFile, ext_file, null);
            }

            for (String string : strings) {
                sbPath.append("/" + string);
            }

            ProtoCommon.activeTest(trackerServer.getSocket());
        }finally {
            trackerServer.close();
        }
        return sbPath.toString();
    }

    /**
     * 文件下载
     *
     * @param groupName
     * @param remoteFileName
     * @return returned value comment here
     */

    public ResponseEntity<byte[]> downloadOriginalo(String groupName,
                                                           String remoteFileName, String specFileName) {
        byte[] content = null;
        HttpHeaders headers = new HttpHeaders();
        try {
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            content = storageClient.download_file(groupName, remoteFileName);
            headers.setContentDispositionFormData("attachment", new String(specFileName.getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

    public byte[] downloadOriginal(String groupName,
                                          String remoteFileName) {
        byte[] b = null;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            b = storageClient.download_file(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return b;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getTracker_http_port() {
        return tracker_http_port;
    }

    public void setTracker_http_port(int tracker_http_port) {
        this.tracker_http_port = tracker_http_port;
    }

    public boolean isAnti_steal_token() {
        return anti_steal_token;
    }

    public void setAnti_steal_token(boolean anti_steal_token) {
        this.anti_steal_token = anti_steal_token;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getTracker_server() {
        return tracker_server;
    }

    public void setTracker_server(String tracker_server) {
        this.tracker_server = tracker_server;
    }

    public String getTracker_ngnix_addr() {
        return tracker_ngnix_addr;
    }

    public void setTracker_ngnix_addr(String tracker_ngnix_addr) {
        this.tracker_ngnix_addr = tracker_ngnix_addr;
    }
}
