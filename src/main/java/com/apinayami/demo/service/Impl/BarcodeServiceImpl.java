package com.apinayami.demo.service.Impl;

import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.service.IBarcodeService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BarcodeServiceImpl implements IBarcodeService {
    private final IProductRepository repository;

    @Override
    public byte[] generateQR(Long id) throws IOException, DocumentException {
        ProductModel model = repository.findById(id).orElseThrow(() -> new CustomException("Product is not exist"));
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        String formattedPrice = currencyVN.format(model.getUnitPrice()) + " VND";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Paragraph("Tên SP: " + model.getProductName(), new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)));
        document.add(new Paragraph("Giá: " + formattedPrice, new Font(Font.FontFamily.HELVETICA, 13)));
        document.add(new Paragraph(" "));


        BufferedImage barcodeImage = generateBarcode("SKU" + String.valueOf(id));
        ByteArrayOutputStream barcodeBaos = new ByteArrayOutputStream();
        ImageIO.write(barcodeImage, "png", barcodeBaos);
        Image barcode = Image.getInstance(barcodeBaos.toByteArray());

        barcode.scalePercent(150);
        document.add(barcode);

        document.close();

        return baos.toByteArray();
    }

    public BufferedImage generateBarcode(String code) throws IOException {
        Code128Bean barcodeBean = new Code128Bean();
        barcodeBean.setModuleWidth(0.3);
        barcodeBean.setBarHeight(15);
        barcodeBean.setFontSize(4);
        barcodeBean.doQuietZone(true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                out, "image/png", 300,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);

        barcodeBean.generateBarcode(canvas, code);
        canvas.finish();

        return canvas.getBufferedImage();
    }
}
