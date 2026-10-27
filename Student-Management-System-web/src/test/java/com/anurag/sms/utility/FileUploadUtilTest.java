package com.anurag.sms.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

/**
 * Photo uploads (#7, #46): only real JPEG and PNG images are stored, under
 * a name made here, and a picture is measured before it is decoded.
 */
class FileUploadUtilTest {

    @TempDir
    Path folder;

    private static MockMultipartFile png(byte[] bytes) {
        return new MockMultipartFile("photoFile", "photo.png", "image/png", bytes);
    }

    @Test
    void aRealPngIsSavedUnderAGeneratedName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), "png", out);

        String name = FileUploadUtil.saveImage(png(out.toByteArray()), folder.toString());

        assertThat(name).endsWith(".png").isNotEqualTo("photo.png");
        assertThat(Files.exists(folder.resolve(name))).isTrue();
    }

    @Test
    void aTextFileNamedLikeAPhotoIsRefused() {
        assertThatThrownBy(() -> FileUploadUtil.saveImage(
                png("<script>alert(1)</script>".getBytes(StandardCharsets.UTF_8)), folder.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FileUploadUtil.NOT_AN_IMAGE);
    }

    // #46: a small file can declare a huge picture. Decoding allocates the
    // whole picture first, so 10000 x 10000 pixels took 100 MB of memory
    // for a file of a few bytes, and a large enough one ran the server out
    // of memory.
    @Test
    void aPictureWithTooManyPixelsIsRefusedBeforeDecoding() throws IOException {
        assertThatThrownBy(() -> FileUploadUtil.saveImage(png(pngHeader(10_000, 10_000)), folder.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FileUploadUtil.TOO_MANY_PIXELS);

        try (var saved = Files.list(folder)) {
            assertThat(saved).isEmpty();
        }
    }

    // A greyscale PNG that declares width x height but holds one row of data
    private static byte[] pngHeader(int width, int height) throws IOException {
        ByteArrayOutputStream file = new ByteArrayOutputStream();
        file.write(new byte[] {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'});

        ByteArrayOutputStream ihdr = new ByteArrayOutputStream();
        DataOutputStream header = new DataOutputStream(ihdr);
        header.writeInt(width);
        header.writeInt(height);
        header.write(new byte[] {8, 0, 0, 0, 0}); // 8-bit greyscale, no interlace
        chunk(file, "IHDR", ihdr.toByteArray());

        ByteArrayOutputStream idat = new ByteArrayOutputStream();
        try (DeflaterOutputStream zip = new DeflaterOutputStream(idat, new Deflater())) {
            zip.write(new byte[width + 1]);
        }
        chunk(file, "IDAT", idat.toByteArray());
        chunk(file, "IEND", new byte[0]);
        return file.toByteArray();
    }

    private static void chunk(ByteArrayOutputStream file, String type, byte[] data) throws IOException {
        DataOutputStream out = new DataOutputStream(file);
        byte[] name = type.getBytes(StandardCharsets.US_ASCII);
        CRC32 crc = new CRC32();
        crc.update(name);
        crc.update(data);
        out.writeInt(data.length);
        out.write(name);
        out.write(data);
        out.writeInt((int) crc.getValue());
    }
}
