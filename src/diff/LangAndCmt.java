package diff;

class LangAndCmt {
	private String lang;
	private String stCmt;
	private String edCmt;

	LangAndCmt(String lang, String stCmt, String edCmt) {
		this.lang = lang;
		this.stCmt = stCmt;
		this.edCmt = edCmt;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setStCmt(String stCmt) {
		this.stCmt = stCmt;
	}

	public String getStCmt() {
		return stCmt;
	}

	public void setEdCmt(String edCmt) {
		this.edCmt = edCmt;
	}

	public String getEdCmt() {
		return edCmt;
	}
}
