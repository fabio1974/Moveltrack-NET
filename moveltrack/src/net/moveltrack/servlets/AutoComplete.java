package net.moveltrack.servlets;

import java.util.List;

public class AutoComplete {
	public List<Obj> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<Obj> suggestions) {
		this.suggestions = suggestions;
	}

	private List<Obj> suggestions;
	private String query = "Unit";
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
