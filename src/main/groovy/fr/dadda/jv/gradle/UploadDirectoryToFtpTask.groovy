package fr.dadda.jv.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.apache.commons.net.ftp.FTPClient

class UploadDirectoryToFtpTask extends DefaultTask {
    final String TAG = "UploadDirectoryToFtpTask"

    def String host
    def String username
    def String password
    def String remotePath
    def String localPath

    def boolean localActive = false

    UploadDirectoryToFtpTask() {
        group = "Publishing"
        description = "Upload a folder to FTP repository"
    }

    @TaskAction
    def uploadDirectory() {
        def ftp = new FTPClient()
        ftp.connect host
        if (localActive) {
            ftp.enterLocalActiveMode()
        } else {
            ftp.enterLocalPassiveMode()
        }
        ftp.login(username, password)
        makeDirectories(ftp, remotePath)
        uploadDirectory(ftp, remotePath, localPath, "")
        ftp.disconnect()
    }

    def uploadDirectory(FTPClient ftpClient, String remoteDirPath, String localParentDir, String remoteParentDir) {
        log "Listing directory: " + localParentDir

        File localDir = project.file(localParentDir)
        File[] subFiles = localDir.listFiles()
        if (subFiles != null && subFiles.length > 0) {
            for (File item : subFiles) {
                String remoteFilePath = remoteDirPath + "/" + remoteParentDir + "/" + item.getName()
                if (remoteParentDir.equals("")) {
                    remoteFilePath = remoteDirPath + "/" + item.getName()
                }

                if (item.isFile()) {
                    if (uploadSingleFile(ftpClient, item, remoteFilePath)) {
                        log "Upload: " + remoteFilePath
                    } else {
                        log "Upload failed for: " + item.getAbsolutePath()
                    }
                } else {
                    if (ftpClient.makeDirectory(remoteFilePath)) {
                        log "Create: " + remoteFilePath
                    } else {
                        log "Create failed for: " + remoteFilePath
                    }

                    String parent = remoteParentDir + "/" + item.getName()
                    if (remoteParentDir.equals("")) {
                        parent = item.getName()
                    }

                    localParentDir = item.getAbsolutePath()
                    uploadDirectory(ftpClient, remoteDirPath, localParentDir, parent)
                }
            }
        }
    }

    def boolean uploadSingleFile(FTPClient ftpClient, File localFile, String remoteFilePath) throws IOException {
        InputStream inputStream = new FileInputStream(localFile)
        try {
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            return ftpClient.storeFile(remoteFilePath, inputStream)
        } finally {
            inputStream.close()
        }
    }

    def boolean makeDirectories(FTPClient ftpClient, String dirPath) throws IOException {
        String[] pathElements = dirPath.split("/")
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
                if (singleDir.equals("")) {
                    continue
                }
                if (!ftpClient.changeWorkingDirectory(singleDir)) {
                    if (ftpClient.makeDirectory(singleDir)) {
                        log "Create: " + singleDir
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        log "Create failed for: " + singleDir
                        return false
                    }
                }
            }
        }
        return true;
    }

    def log(String message) {
        println TAG + "|" + message
    }

}