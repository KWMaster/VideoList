package lvlw.com.myvideolist.entity;

/**
 * @author qfc
 *
 */
public final class QFileInfo {

	// file icon res
	private int _fileIcon;
	// file name
	private String _fileName;
	// file absolute path
	private String _filePath;
	// file description
	private String _fileDesc;
	// file extention
	private String _fileExt;
	//file lenght
	private String _fileSize;
	//file SQLiteId
	private long _fileId;

	public String get_fileSize() {
		return _fileSize;
	}

	public void set_fileSize(String _fileSize) {
		this._fileSize = _fileSize;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}

	private boolean isCheck=false;

	/**
	 * @param fileIcon
	 * @param fileName
	 * @param filePath
	 * @param fileDesc
	 */
	public QFileInfo(int fileIcon, String fileName, String filePath,
                     String fileDesc, boolean isCheck) {
		_fileIcon = fileIcon;
		_fileName = fileName;
		_filePath = filePath;
		_fileDesc = fileDesc;
		this.isCheck=isCheck;
	}


	public QFileInfo() {

	}
	public long get_fileId() {
		return _fileId;
	}

	public void set_fileId(long _fileId) {
		this._fileId = _fileId;
	}

	public int get_fileIcon() {
		return _fileIcon;
	}

	public void set_fileIcon(int _fileIcon) {
		this._fileIcon = _fileIcon;
	}

	public String get_fileName() {
		return _fileName;
	}

	public void set_fileName(String _fileName) {
		this._fileName = _fileName;
	}

	public String get_filePath() {
		return _filePath;
	}

	public void set_filePath(String _filePath) {
		this._filePath = _filePath;
	}
	
	public String get_fileDesc() {
		return _fileDesc;
	}


	public void set_fileDesc(String _fileDesc) {
		this._fileDesc = _fileDesc;
	}
	

	public String get_fileExt() {
		return _fileExt;
	}


	public void set_fileExt(String _fileExt) {
		this._fileExt = _fileExt;
	}
}