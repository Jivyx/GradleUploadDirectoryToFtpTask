# GradleUploadDirectoryToFtpTask
Gradle Task to upload a directory to a distant FTP. Use FTPClient implementation from Apache Commons Net

#Integration
Add this dependency to your root build.gradle:

```
buildscript {
    repositories {
        ...
        maven { url 'http://dl.bintray.com/jivy/maven' }
    }
    dependencies {
        ...
        classpath 'fr.dadda.jv.gradle:GradleUploadDirectoryToFtpTask:1.0'
    }
}
```

# Usage
Define a new task of type `fr.dadda.jv.gradle.UploadDirectoryToFtpTask` in your project build.gradle:

```
task uploadJavadocToFtp(type: fr.dadda.jv.gradle.UploadDirectoryToFtpTask) {
    host "192.168.0.123"
    username "user"
    password "password"
    remotePath "/javadoc/"
    localPath "build/docs/javadoc"
}
```
