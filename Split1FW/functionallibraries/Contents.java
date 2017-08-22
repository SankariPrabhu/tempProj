package functionallibraries;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Contents extends ReusableLibrary {

	public Contents(ScriptHelper scriptHelper) {
		super(scriptHelper);
		if (!driver.getCurrentUrl().contains("contents")) {
			throw new IllegalStateException(
					"Contents page is expected, but not displayed!");
		}
	}
}
