package shcompany.LowPrice2;

import java.io.Serializable;

/**
 * Parcelable 인터페이스를 구현하는 클래스 정의
 *
 * @author Mike
 */
public class SimpleData implements Serializable{
	private int price;
	private String strSearch;
	private String strImgPath;


	public String getStrImgPath() {
		return strImgPath;
	}

	public void setStrImgPath(String strImgPath) {
		this.strImgPath = strImgPath;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getStrSearch() {
		return strSearch;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}
}
