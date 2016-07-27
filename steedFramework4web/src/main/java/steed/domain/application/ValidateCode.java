package steed.domain.application;

import java.awt.image.BufferedImage;
/**
 * 验证码实体类
 * @author 战马
 *
 */
public class ValidateCode {
	private String code;
	private BufferedImage codeImg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BufferedImage getCodeImg() {
		return codeImg;
	}
	public void setCodeImg(BufferedImage codeImg) {
		this.codeImg = codeImg;
	}
}
