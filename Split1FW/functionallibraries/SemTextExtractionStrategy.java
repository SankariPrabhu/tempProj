package functionallibraries;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class SemTextExtractionStrategy implements TextExtractionStrategy {

	private String text;

	@Override
	public void beginTextBlock() {
	}

	@Override
	public void renderText(TextRenderInfo renderInfo) {
		text = renderInfo.getText();
		System.out.println("Text " + text);
		String font[][] = renderInfo.getFont().getFullFontName();
		String fontName = font[0][3];
		System.out.println("Font Name " + fontName);
		Vector curBaseline = renderInfo.getBaseline().getStartPoint();
		Vector topRight = renderInfo.getAscentLine().getEndPoint();

		Rectangle rect = new Rectangle(curBaseline.get(0), curBaseline.get(1), topRight.get(0), topRight.get(1));
		float curFontSize = rect.getHeight();
		System.out.println("Font Size " + curFontSize);
		this.setResultantText(fontName);

	}

	@Override
	public void endTextBlock() {
	}

	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
	}

	@Override
	public String getResultantText() {
		return text;
	}

	public void setResultantText(String value) {
		this.text = value;
	}

}