package de.mca.extensions.eclipse.interfaces;
import java.util.List;

public interface APIEntry {
	public List<APIEntry> find(String input);
	public String getName();
	public String getDescription();
}
