package diff;

public class DiffInfo {
	private String oldPath;
	private String oldSource;
	private String newPath;
	private String newSource;
	private int oldSteps;
	private int editSteps;
	private int addSteps;
	private int deleteSteps;
	private int newSteps;
	boolean enable;
	public DiffInfo(String oldPath,String oldSource,String newPath,String newSource,int oldSteps,int editSteps,int addSteps,int deleteSteps,int newSteps){
		this.oldPath = oldPath;
		this.oldSource = oldSource;
		this.newPath = newPath;
		this.newSource = newSource;
		this.oldSteps = oldSteps;
		this.editSteps = editSteps;
		this.addSteps = addSteps;
		this.deleteSteps = deleteSteps;
		this.newSteps = newSteps;
		if(oldSteps != 0 && newSteps != 0){
			enable = true;
		}
	}
	public String getOldPath() {
		return oldPath;
	}
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public String getOldSource() {
		return oldSource;
	}
	public void setOldSource(String oldSource) {
		this.oldSource = oldSource;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	public String getNewSource() {
		return newSource;
	}
	public void setNewSource(String newSource) {
		this.newSource = newSource;
	}
	public int getOldSteps() {
		return oldSteps;
	}
	public void setOldSteps(int oldSteps) {
		this.oldSteps = oldSteps;
	}
	public int getEditSteps() {
		return editSteps;
	}
	public void setEditSteps(int editSteps) {
		this.editSteps = editSteps;
	}
	public int getAddSteps() {
		return addSteps;
	}
	public void setAddSteps(int addSteps) {
		this.addSteps = addSteps;
	}
	public int getDeleteSteps() {
		return deleteSteps;
	}
	public void setDeleteSteps(int deleteSteps) {
		this.deleteSteps = deleteSteps;
	}

	public int getNewSteps() {
		return newSteps;
	}
	public void setNewSteps(int newSteps) {
		this.newSteps = newSteps;
	}
}
