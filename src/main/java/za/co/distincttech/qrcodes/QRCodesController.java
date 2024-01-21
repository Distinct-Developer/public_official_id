package za.co.distincttech.qrcodes;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestMapping("qrcodes")
@RestController
public class QRCodesController {

    private final Logger LOGGER = Logger.getLogger(QRCodesController.class.getName());

    private BitMatrix bitMatrix;
    private BinaryBitmap binaryBitmap;
    private String fileName;

    @GetMapping("/")
    public ResponseEntity<String> defaultPath() {

        LOGGER.log(Level.INFO, "Reached the QRCodes controller");
        return ResponseEntity.ok("Reached the QRCodes controller");
    }

    @GetMapping(value = "/generate")
    public ResponseEntity<String> generateZxingQrCode() {

        LOGGER.log(Level.INFO, "Reached the QRCodes controller");
//        final String data = "https://www.distincttech.co.za";
        final String data = String.valueOf(new Date());

        // Encoding charset
        final String charset = "UTF-8", fileType = "png";
        fileName = "generated_qrcode_date.png";
        final int width = 200, height = 200;

        try {
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, fileType, Paths.get(new File(fileName).toURI()));
        } catch (WriterException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return ResponseEntity.ok("QR Code successfully generated.");
    }

    @GetMapping(value = "/read")
    public ResponseEntity<String> readZxingQrCode() {

        LOGGER.log(Level.INFO, "Reached the QRCodes controller");
//        final String data = "https://www.distincttech.co.za";
        final String data = String.valueOf(new Date());

        // Encoding charset
        final String charset = "UTF-8", fileType = "png";
        fileName = "generated_qrcode_date.png";
        final int width = 200, height = 200;

        try {
            binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(
                                    ImageIO.read(new FileInputStream(fileName))
                            )
                    )
            );
            Result output = new MultiFormatReader().decode(binaryBitmap);
            LOGGER.log(Level.INFO, output.getText());
        } catch (ReaderException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return ResponseEntity.ok("QR Code successfully read.");
    }
}
