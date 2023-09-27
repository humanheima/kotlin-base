package base

import java.io.File

/**
 * Created by dumingwei on 2023/9/27
 */
class FileTest {
}

fun main() {
    val file = File("/Users/dumingwei/Downloads/temp_zip_2")
    val size = getFolderSize(file)
    println("size=$size")
    println("formatFileSize(size)=${formatFileSize(size)}")
}

private fun getFolderSize(folder: File): Long {
    var size: Long = 0
    val files = folder.listFiles()
    if (files != null) {
        for (file in files) {
            if (file.isFile) {
                size += file.length()
            } else {
                size += getFolderSize(file)
            }
        }
    }
    return size
}

/**
 * @param size 单位是字节
 */
private fun formatFileSize(size: Long): String {
    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return "$size B"
    }
    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        return "$kiloByte KB"
    }
    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        return "$megaByte MB"
    }
    val teraByte = gigaByte / 1024
    if (teraByte < 1) {
        return "$gigaByte GB"
    }
    return "$teraByte TB"
}