package UIMAP;

import org.openqa.selenium.By;

public class UIMAP_GUIBPageeditor {
	public static By txtTitle = By.cssSelector("input[data-key='title']");
	public static By txtComment = By.cssSelector("input[data-key='comments']");
	public static By txtClearCustomer = By.cssSelector("input[data-action='clearcustomer']");
	public static By liPods = By.cssSelector("li[class*='pod-list-item']");
}
