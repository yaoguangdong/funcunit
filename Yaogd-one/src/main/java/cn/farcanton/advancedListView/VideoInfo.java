package cn.farcanton.advancedListView;
/**
 * 列表数据模型
 * @author Administrator
 *
 */
public class VideoInfo {

	private String v_id;
	private String v_name;
	private int v_size;
	private String v_url;
	private String v_imageUrl;
	
	public String getV_id() {
		return v_id;
	}
	public void setV_id(String v_id) {
		this.v_id = v_id;
	}
	public String getV_name() {
		return v_name;
	}
	public void setV_name(String v_name) {
		this.v_name = v_name;
	}
	public int getV_size() {
		return v_size;
	}
	public void setV_size(int v_size) {
		this.v_size = v_size;
	}
	public String getV_url() {
		return v_url;
	}
	public void setV_url(String v_url) {
		this.v_url = v_url;
	}
	public String getV_imageUrl() {
		return v_imageUrl;
	}
	public void setV_imageUrl(String v_imageUrl) {
		this.v_imageUrl = v_imageUrl;
	}
	public VideoInfo(String v_id, String v_name, int v_size, String v_url,
			String v_imageUrl) {
		super();
		this.v_id = v_id;
		this.v_name = v_name;
		this.v_size = v_size;
		this.v_url = v_url;
		this.v_imageUrl = v_imageUrl;
	}
	
}
