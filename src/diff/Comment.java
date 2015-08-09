package diff;

public class Comment {
	private String startCmt;
	private String endCmt;

	Comment(String startCmt) {
		this.startCmt = startCmt;
	}
	
	Comment(String startCmt, String endCmt) {
		this.startCmt = startCmt;
		this.endCmt = endCmt;
	}

	public String getStartCmt() {
		return startCmt;
	}

	public void setStartCmt(String startCmt) {
		this.startCmt = startCmt;
	}

	public String getEndCmt() {
		return endCmt;
	}

	public void setEndCmt(String endCmt) {
		this.endCmt = endCmt;
	}

}
