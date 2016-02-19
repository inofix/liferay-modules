package ch.inofix.portlet.contact.dto;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-15 18:42
 * @modified 2015-05-15 18:42
 * @version 1.0.0
 */
public class ImppDTO extends BaseDTO {

	String protocol = "";
//	String type = "";
	String uri = "";

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
