package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class VSImage extends VSpaceElement implements IVSImage {

	@Id 
	@GeneratedValue(generator = "image_id_generator")
    @GenericGenerator(name = "image_id_generator", 
      parameters = @Parameter(name = "prefix", value = "IMG"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	@Lob private String filename;
	@Lob private String parentPath;
	private String fileType;
	
	private int height;
	private int width;
	
	private String tag;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#getFilename()
	 */
	@Override
	public String getFilename() {
		return filename;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#setFilename(java.lang.String)
	 */
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#getParentPath()
	 */
	@Override
	public String getParentPath() {
		return parentPath;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IImage#setParentPath(java.lang.String)
	 */
	@Override
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	@Override
	public String getFileType() {
		return fileType;
	}
	@Override
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public void setWidth(int width) {
		this.width = width;
	}	
	
	@Override
    public String getTag() {
        return tag;
    }
    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
