package com.anurag.sms.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * Stores student and teacher photos safely (#7).
 *
 * Uploads are served from the same origin as the app, so an uploaded .html
 * or .svg file would run as stored XSS. Every file is therefore decoded as a
 * real JPEG or PNG image, and its name is generated here: the name the
 * browser sent is never used.
 */
public final class FileUploadUtil {

    static final String NOT_ALLOWED = "Only JPG or PNG photos are allowed.";
    static final String NOT_AN_IMAGE = "That file is not a valid JPG or PNG image.";

    private static final Set<String> CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    // Image format detected from the file's bytes -> extension we save it with
    private static final Map<String, String> EXTENSIONS = Map.of("jpeg", ".jpg", "png", ".png");

    private FileUploadUtil() {
    }

    /**
     * Validates the upload and writes it into {@code folder} under a new random name.
     *
     * @return the generated file name, to store on the entity
     * @throws IllegalArgumentException if the file is not a real JPEG or PNG image;
     *                                  the message is safe to show to the user
     * @throws IOException              if the file cannot be written
     */
    public static String saveImage(MultipartFile file, String folder) throws IOException {

        String format = detectImageFormat(file);
        String fileName = UUID.randomUUID() + EXTENSIONS.get(format);

        Path dir = Paths.get(folder);
        Files.createDirectories(dir);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dir.resolve(fileName));
        }

        return fileName;
    }

    /**
     * Deletes a previously saved photo. Does nothing for a blank name or one that
     * is not a plain file name, so a stored value can never point outside
     * {@code folder}.
     */
    public static void delete(String folder, String fileName) throws IOException {

        if (fileName == null || fileName.isBlank()) {
            return;
        }

        Path dir = Paths.get(folder).toAbsolutePath().normalize();
        Path target = dir.resolve(fileName).normalize();

        if (!target.getParent().equals(dir)) {
            return;
        }

        Files.deleteIfExists(target);
    }

    // Reads the bytes, not the name: the content type is only a first filter,
    // because the browser derives it from the file extension.
    private static String detectImageFormat(MultipartFile file) throws IOException {

        if (!CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException(NOT_ALLOWED);
        }

        try (ImageInputStream in = ImageIO.createImageInputStream(file.getInputStream())) {

            Iterator<ImageReader> readers = in == null ? null : ImageIO.getImageReaders(in);
            if (readers == null || !readers.hasNext()) {
                throw new IllegalArgumentException(NOT_AN_IMAGE);
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(in);
                String format = reader.getFormatName().toLowerCase(Locale.ROOT);
                if (!EXTENSIONS.containsKey(format)) {
                    throw new IllegalArgumentException(NOT_ALLOWED);
                }

                // Decode it fully: a file that merely starts like an image
                // fails here, with an IOException or a runtime error.
                try {
                    reader.read(0);
                } catch (IOException | RuntimeException e) {
                    throw new IllegalArgumentException(NOT_AN_IMAGE, e);
                }
                return format;

            } finally {
                reader.dispose();
            }
        }
    }
}
