package your_package_name.tapestry.services;

import java.io.IOException;

import org.apache.tapestry5.ioc.Resource;

/**
 * 
 * @author nmeylan
 */
public interface FileSystemAssetResourceLocator {
	public Resource findClasspathResourceForPath(String path) throws IOException;
}
