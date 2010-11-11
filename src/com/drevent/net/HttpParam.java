/**
 * 
 */
package com.drevent.net;

/**
 * The URL query param e.g. url?query=value.
 * 
 * @author Izzet_Mustafayev
 * 
 */
public class HttpParam {

	private String query;
	private String value;

	public HttpParam(final String query, final String value) {
		super();
		this.query = query;
		this.value = value;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpParam other = (HttpParam) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
