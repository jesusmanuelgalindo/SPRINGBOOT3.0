package gob.jmas.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.*;

public class GenerarQR {

    @Value("${qr.defaultWidth}")
    private int defaultWidth;

    @Value("${qr.defaultHeight}")
    private int defaultHeight;

    public  BufferedImage imagen(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, defaultWidth, defaultHeight, hints);
        BufferedImage image = new BufferedImage(defaultWidth, defaultHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < defaultWidth; x++) {
            for (int y = 0; y < defaultHeight; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK.getRGB() : WHITE.getRGB());
            }
        }
        return image;
    }

    public  byte[] bytes(String text) throws WriterException, IOException {
        BufferedImage image = imagen(text);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
