# GradleUploadDirectoryToFtpTask
Gradle Task to upload a directory to a distant FTP. Use FTPClient implementation from Apache Commons Net

# Usage

Define a new task of type `fr.dadda.jv.gradle.UploadDirectoryToFtpTask`.

```
task uploadJavadocToFtp(type: fr.dadda.jv.gradle.UploadDirectoryToFtpTask) {
    host "192.168.0.123"
    username "user"
    password "password"
    remotePath "/javadoc/"
    localPath "build/docs/javadoc"
}
```
