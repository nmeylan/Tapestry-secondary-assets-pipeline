package your_package_name.tapestry.services;

/**
 * 
 * @author nmeylan
 */
public interface FileSystemAssetAliasManager {
	String toClientURL(String resourcePath);

	String toResourcePath(String clientURL);
}
