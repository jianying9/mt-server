/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xiezhutech.mt.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

/**
 *
 * @author jianying9
 */
public class FontImageJUnitTest {

    public FontImageJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here. 
    // The methods must be annotated with annotation @Test. For example:
    //
    private int drawLogoBi(Graphics2D g2, int startY, int biWidth, int biPaddingLeft, int rate) {
        String value = "/Users/jianying9/NetBeansProjects/mt_1x.png";
        switch (rate) {
            case 2:
                value = value.replace("_1x", "_2x");
                break;
            case 3:
                value = value.replace("_1x", "_3x");
                break;
            case 4:
                value = value.replace("_1x", "_4x");
                break;
            case 5:
                value = value.replace("_1x", "_5x");
                break;
        }
        try {
            BufferedImage logoBi = ImageIO.read(new File(value));
            int x = (biWidth - logoBi.getWidth()) / 2;
            //
            int y = startY;
            g2.drawImage(logoBi, x, y, logoBi.getWidth(), logoBi.getHeight(), null);
            startY = y + logoBi.getHeight();
        } catch (IOException e) {
        }
        return startY;
    }

    private int drawCodeBi(Graphics2D g2, int startY, int biWidth, int biPaddingLeft, int rate) {
        String code = "0000010001";
        int height = 10 * rate;
        int fontSize = 2 * rate;
        int dpi = 360;
        Code128Bean bean = new Code128Bean();
        double mw = bean.getModuleWidth();
        mw = mw * rate;
        bean.setModuleWidth(mw);
        bean.setHeight(height);
        bean.setFontSize(fontSize);
        bean.doQuietZone(false);
        String format = "image/png";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(outStream, format, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);
        BufferedImage codeBi = null;
        try {
            bean.generateBarcode(canvas, code);
            canvas.finish();
            InputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
            codeBi = ImageIO.read(inputStream);
        } catch (IOException ex) {
        }
        if (codeBi != null) {
            int x = (biWidth - codeBi.getWidth()) / 2;
            //
            int y = startY;
            g2.drawImage(codeBi, x, y, codeBi.getWidth(), codeBi.getHeight(), null);
            startY = y + codeBi.getHeight();
        }
        return startY;
    }

    private int drawTextBi(Graphics2D g2, int startY, int biWidth, int biPaddingLeft, String value, boolean blod, String align, int size, int rate) {
        int style = Font.PLAIN;
        if (blod) {
            style = Font.BOLD;
        }
        //
        size = size * rate;
        Font font = new Font("PingFang SC", style, size);
        g2.setFont(font);
        //
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(value, context);
        //
        int x = biPaddingLeft;
        if (align != null && align.equals("center")) {
            x = (biWidth - (int) bounds.getWidth()) / 2;
        }
        int ascent = (int) (-bounds.getY());
        int y = startY + ascent;
        g2.drawString(value, x, y);
        startY = y;
        return startY;
    }

    @Test
    public void hello() throws IOException {
        int width = 384;
        int height = 537;
        int paddingLeft = 30;
        int lineHeight = 6;
        int rate = 4;
        int biWidth = width * rate;
        int biHeight = height * rate;
        int biPaddingLeft = paddingLeft * rate;
        int biLineHeight = lineHeight * rate;
        //
        BufferedImage bi = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, biWidth, biHeight);
        g2.setPaint(Color.BLACK);
        //logo
        int startY = 0;
        startY = this.drawLogoBi(g2, startY, biWidth, biPaddingLeft, rate);
        startY = startY + biLineHeight;
        //条形码
        startY = this.drawCodeBi(g2, startY, biWidth, biPaddingLeft, rate);
        startY = startY + biLineHeight;
        //
        String content = "钢筋";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, true, "center", 40, rate);
        startY = startY + biLineHeight;
        //
        content = "张三(受理)";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "center", 20, rate);
        startY = startY + biLineHeight;
        //
        content = "福州市轨道交通5号线一期工程施工";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "left", 20, rate);
        startY = startY + biLineHeight;
        //
        content = "总承包第一标段土建2工区农林大学站";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "left", 20, rate);
        startY = startY + biLineHeight;
        //
        content = "直径规格:$32mm";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "left", 20, rate);
        startY = startY + biLineHeight;
        //
        content = "样品数量:10根";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "left", 20, rate);
        startY = startY + biLineHeight;
        //
        content = "牌号炉号:HRB400E";
        startY = this.drawTextBi(g2, startY, biWidth, biPaddingLeft, content, false, "left", 20, rate);
        startY = startY + biLineHeight;
        //
        if (rate > 1) {
            double ratio = 1d / rate;
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            bi = op.filter(bi, null);
        }
        File file = new File("/Users/jianying9/NetBeansProjects/10_" + rate + ".jpg");
        ImageIO.write(bi, "JPG", file);
    }
}
