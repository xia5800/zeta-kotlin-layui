package org.zetaframework.extra.file.util

import cn.hutool.core.codec.Base64Decoder
import cn.hutool.core.lang.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.*


/**
 * base64文件工具类
 *
 * @author gcc
 */
object Base64FileUtil {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Base64转MultipartFile
     *
     * @param base64 String
     * @return MultipartFile
     */
    fun base64Convert(base64: String): MultipartFile {
        val base64Array = base64.split(",")

        var content = ByteArray(0)
        try {
            content = Base64Decoder.decode(base64Array[1])
        } catch (e: IOException) {
            e.printStackTrace()
            logger.info("base64转byte失败")
        }
        for (i in content.indices) {
            if (content[i] < 0) {
                content[i] = (content[i] + 256).toByte()
            }
        }

        return Base642MultipartFile(base64Array[0], content)
    }

}

/**
 * Base64转MultipartFile
 */
class Base642MultipartFile(
    /** base64前缀，例如：data:image/png;base64, */
    var header: String,
    /** base64内容 */
    var content: ByteArray
) : MultipartFile {

    /**
     * Return an InputStream to read the contents of the file from.
     *
     * The user is responsible for closing the returned stream.
     * @return the contents of the file as stream, or an empty stream if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(content)
    }

    /**
     * Return the name of the parameter in the multipart form.
     * @return the name of the parameter (never `null` or empty)
     */
    override fun getName(): String {
        val simpleUUID = UUID.randomUUID(false).toString(true)

        // 获取文件后缀
        var suffix = header.substring(11, header.indexOf(";"))
        if (suffix.isNotBlank()) {
            suffix = ".$suffix"
        }
        return "${simpleUUID}${suffix}"
    }

    /**
     * Return the original filename in the client's filesystem.
     *
     * This may contain path information depending on the browser used,
     * but it typically will not with any other than Opera.
     *
     * **Note:** Please keep in mind this filename is supplied
     * by the client and should not be used blindly. In addition to not using
     * the directory portion, the file name could also contain characters such
     * as ".." and others that can be used maliciously. It is recommended to not
     * use this filename directly. Preferably generate a unique one and save
     * this one somewhere for reference, if necessary.
     * @return the original filename, or the empty String if no file has been chosen
     * in the multipart form, or `null` if not defined or not available
     * @see org.apache.commons.fileupload.FileItem.getName
     * @see org.springframework.web.multipart.commons.CommonsMultipartFile.setPreserveFilename
     *
     * @see [RFC 7578, Section 4.2](https://tools.ietf.org/html/rfc7578.section-4.2)
     *
     * @see [Unrestricted File Upload](https://owasp.org/www-community/vulnerabilities/Unrestricted_File_Upload)
     */
    override fun getOriginalFilename(): String? {
        // 等价于 return getName()
        return name
    }

    /**
     * Return the content type of the file.
     * @return the content type, or `null` if not defined
     * (or no file has been chosen in the multipart form)
     */
    override fun getContentType(): String? {
        return header.substring(5, header.indexOf(";"))
    }

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    override fun isEmpty(): Boolean {
        return content.isEmpty()
    }

    /**
     * Return the size of the file in bytes.
     * @return the size of the file, or 0 if empty
     */
    override fun getSize(): Long {
        return content.size.toLong()
    }

    /**
     * Return the contents of the file as an array of bytes.
     * @return the contents of the file as bytes, or an empty byte array if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    override fun getBytes(): ByteArray {
        return content
    }

    /**
     * Transfer the received file to the given destination file.
     *
     * This may either move the file in the filesystem, copy the file in the
     * filesystem, or save memory-held contents to the destination file. If the
     * destination file already exists, it will be deleted first.
     *
     * If the target file has been moved in the filesystem, this operation
     * cannot be invoked again afterwards. Therefore, call this method just once
     * in order to work with any storage mechanism.
     *
     * **NOTE:** Depending on the underlying provider, temporary storage
     * may be container-dependent, including the base directory for relative
     * destinations specified here (e.g. with Servlet 3.0 multipart handling).
     * For absolute destinations, the target file may get renamed/moved from its
     * temporary location or newly copied, even if a temporary copy already exists.
     * @param dest the destination file (typically absolute)
     * @throws IOException in case of reading or writing errors
     * @throws IllegalStateException if the file has already been moved
     * in the filesystem and is not available anymore for another transfer
     * @see org.apache.commons.fileupload.FileItem.write
     * @see javax.servlet.http.Part.write
     */
    override fun transferTo(dest: File) {
        FileOutputStream(dest).write(bytes)
    }
}
