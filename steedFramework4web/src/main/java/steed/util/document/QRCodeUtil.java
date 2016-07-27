package steed.util.document;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import steed.exception.runtime.system.FrameworkException;

/**
 * 
 * @author 战马
 *
 */
public class QRCodeUtil {
	
	public File getQrCode(String content,int width, int height, String format){
		File temp = null;
		try {
			temp = File.createTempFile(content, "."+format);
			getQrCode(temp.getAbsolutePath(), content, width, height, format);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (temp != null) {
				temp.deleteOnExit();
			}
		}
		return temp;
	}
	
	/**
	 * 获取中间带图标的二维码
	 * @param content 二维码内容
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 * @param frameWidth 边框大小
	 * @param imageFile 中间图标图片文件
	 * @param srcImageWidth 中间图标宽度
	 * @param srcImageHeight 中间图标高度
	 * @return BufferedImage 可以用ImageIO.write(qrcode, "jpg", new File("D:/ttt.jpg"));生成图片,也可以用ImageIO.write的其它重载方法
	 */
	public static BufferedImage getQrcode(String content, int width, int height,int frameWidth,File imageFile,int srcImageWidth, int srcImageHeight){
		// 读取源图像  
        BufferedImage scaleImage;
		try {
			scaleImage = scale(imageFile, srcImageWidth,  
					srcImageHeight, true);
			int[][] srcPixels = new int[srcImageWidth][srcImageHeight];  
			for (int i = 0; i < scaleImage.getWidth(); i++) {  
				for (int j = 0; j < scaleImage.getHeight(); j++) {  
					srcPixels[i][j] = scaleImage.getRGB(i, j);  
				}  
			}
			BitMatrix matrix = getBitMatrix(content, width, height, true);
			
			int[] pixels = getPixels(width, height, frameWidth, srcImageWidth,
					srcPixels, matrix);  
	  
	        BufferedImage image = new BufferedImage(width, height,  
	                BufferedImage.TYPE_INT_RGB);  
	        image.getRaster().setDataElements(0, 0, width, height, pixels); 
			return image;
		} catch (IOException | WriterException e) {
			e.printStackTrace();
			throw new FrameworkException(e);
		}  
	}

	private static int[] getPixels(int width, int height, int frameWidth,
			int srcImageWidth, int[][] srcPixels, BitMatrix matrix) {
		// 二维矩阵转为一维像素数组  
		int halfW = matrix.getWidth() / 2;  
		int halfH = matrix.getHeight() / 2;  
		int[] pixels = new int[width * height];  
		
		int IMAGE_HALF_WIDTH = srcImageWidth / 2;
		
		for (int y = 0; y < matrix.getHeight(); y++) {  
		    for (int x = 0; x < matrix.getWidth(); x++) {  
		        // 读取图片  
		        if (x > halfW - IMAGE_HALF_WIDTH  
		                && x < halfW + IMAGE_HALF_WIDTH  
		                && y > halfH - IMAGE_HALF_WIDTH  
		                && y < halfH + IMAGE_HALF_WIDTH) {  
		            pixels[y * width + x] = srcPixels[x - halfW  
		                    + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];  
		        }   
		        // 在图片四周形成边框  
		        else if ((x > halfW - IMAGE_HALF_WIDTH - frameWidth  
		                && x < halfW - IMAGE_HALF_WIDTH + frameWidth  
		                && y > halfH - IMAGE_HALF_WIDTH - frameWidth && y < halfH  
		                + IMAGE_HALF_WIDTH + frameWidth)  
		                || (x > halfW + IMAGE_HALF_WIDTH - frameWidth  
		                        && x < halfW + IMAGE_HALF_WIDTH + frameWidth  
		                        && y > halfH - IMAGE_HALF_WIDTH - frameWidth && y < halfH  
		                        + IMAGE_HALF_WIDTH + frameWidth)  
		                || (x > halfW - IMAGE_HALF_WIDTH - frameWidth  
		                        && x < halfW + IMAGE_HALF_WIDTH + frameWidth  
		                        && y > halfH - IMAGE_HALF_WIDTH - frameWidth && y < halfH  
		                        - IMAGE_HALF_WIDTH + frameWidth)  
		                || (x > halfW - IMAGE_HALF_WIDTH - frameWidth  
		                        && x < halfW + IMAGE_HALF_WIDTH + frameWidth  
		                        && y > halfH + IMAGE_HALF_WIDTH - frameWidth && y < halfH  
		                        + IMAGE_HALF_WIDTH + frameWidth)) {  
		            pixels[y * width + x] = 0xfffffff;  
		        } else {  
		            // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；  
		            pixels[y * width + x] = matrix.get(x, y) ? 0xff000000  
		                    : 0xfffffff;  
		        }  
		    }  
		}
		return pixels;
	}
	
	 /** 
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标 
     *  
     * @param srcImageFile 
     *            源文件地址 
     * @param height 
     *            目标高度 
     * @param width 
     *            目标宽度 
     * @param hasFiller 
     *            比例不对时是否需要补白：true为补白; false为不补白; 
     * @throws IOException 
     */  
    private static BufferedImage scale(File srcImageFile, int height,int width, boolean hasFiller) throws IOException{  
        double ratio = 0.0; // 缩放比例  
        BufferedImage srcImage = ImageIO.read(srcImageFile);  
        Image destImage = srcImage.getScaledInstance(width, height,  
                BufferedImage.SCALE_SMOOTH);  
        // 计算比例  
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {  
            if (srcImage.getHeight() > srcImage.getWidth()) {  
                ratio = (new Integer(height)).doubleValue()  
                        / srcImage.getHeight();  
            } else {  
                ratio = (new Integer(width)).doubleValue()  
                        / srcImage.getWidth();  
            }  
            AffineTransformOp op = new AffineTransformOp(  
                    AffineTransform.getScaleInstance(ratio, ratio), null);  
            destImage = op.filter(srcImage, null);  
        }  
        if (hasFiller) {// 补白  
            BufferedImage image = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics2D graphic = image.createGraphics();  
            graphic.setColor(Color.white);  
            graphic.fillRect(0, 0, width, height);  
            if (width == destImage.getWidth(null)){  
                graphic.drawImage(destImage, 0,  
                        (height - destImage.getHeight(null)) / 2,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null);  
            }else  {
                graphic.drawImage(destImage,  
                        (width - destImage.getWidth(null)) / 2, 0,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null); 
            }
            graphic.dispose();  
            destImage = image;  
        }  
        return (BufferedImage) destImage;  
    }  
	
	public static void getQrCode(OutputStream out, String content,
			int width, int height, String format) throws WriterException,
			IOException {
		BitMatrix bitMatrix = getBitMatrix(content, width, height,true);
		MatrixToImageWriter.writeToStream(bitMatrix, format, out);
	}
	public static void getQrCode(OutputStream out, String content,
			int width, int height, String format,boolean isQrcode) throws WriterException,
			IOException {
		BitMatrix bitMatrix = getBitMatrix(content, width, height,isQrcode);
		MatrixToImageWriter.writeToStream(bitMatrix, format, out);
	}
	
	public static void getQrCode(String path, String content,
			int width, int height, String format) throws WriterException,
			IOException {
		BitMatrix bitMatrix = getBitMatrix(content, width, height,true);
		MatrixToImageWriter.writeToPath(bitMatrix, format, Paths.get(path));
	}
	public static void getQrCode(String path, String content,
			int width, int height, String format,boolean isQrCode) throws WriterException,
			IOException {
		BitMatrix bitMatrix = getBitMatrix(content, width, height,isQrCode);
		MatrixToImageWriter.writeToPath(bitMatrix, format, Paths.get(path));
	}


	private static BitMatrix getBitMatrix(String content, int width, int height,boolean isQrCode)
			throws WriterException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
//		EncodeHintType.
		BitMatrix bitMatrix;
		if (isQrCode) {
			bitMatrix = new MultiFormatWriter().encode(content,  
					BarcodeFormat.QR_CODE, width, height, hints);
		}else {
			/**
			 * PDF_417
			 * DATA_MATRIX
			 */
			bitMatrix = new MultiFormatWriter().encode(content,  
					BarcodeFormat.CODE_128, width, height, hints);
		}
//		bitMatrix.setRegion(100, 100, 100, 100);
//		bitMatrix.set(50, 50);
		return bitMatrix;
	}
}
