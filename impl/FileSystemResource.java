package your_package_name.tapestry.services.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import your_package_name.tapestry.services.FileSystemAssetAliasManager;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;

/**
 * 
 * @author nmeylan
 */
public class FileSystemResource extends AbstractResource {

	private final FileSystemAssetAliasManager aliasManager;

	/**
	 * @param path
	 *            Path of filesystem resource, it contains filesystem alias.
	 * @param aliasManager
	 *            Alias manager so filesystem symbol in asset path can be
	 *            mapped.
	 * 
	 * @throws IllegalArgumentException
	 *             rootDirectory is null, rootDirectory does not exist.
	 */
	// using URI, so some warranty of string-pattern is given
	public FileSystemResource(FileSystemAssetAliasManager aliasManager, String path) {
		super(path);

		this.aliasManager = aliasManager;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws RuntimeException
	 *             Bad filesystem-path pattern was passed in method
	 *             newResource().
	 */
	@Override
	public URL toURL() {
		URI url = this.transformPathToUrl();
		try {
			if (new File(url).exists()) {
				return url.toURL();
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(String.format("Filesystem resource URL cannot be created, given relative " + "path above filesystem root (%s) has bad pattern", this.getPath()), e);
		}

	}

	private URI transformPathToUrl() {
		URI url;
		try {
			String resourcePath = aliasManager.toResourcePath(this.getPath());
			if (resourcePath.startsWith("fs/")) {
				resourcePath = resourcePath.substring(3);
			}
			url = new URI(resourcePath);
		} catch (URISyntaxException e) {
			throw new RuntimeException(String.format("Filesystem resource URL cannot be created, given relative " + "path above filesystem root (%s) has bad pattern", this.getPath()), e);
		}
		return url;
	}

	@Override
	protected Resource newResource(String path) {
		return new FileSystemResource(aliasManager, path);
	}

	@Override
	public String toString() {
		return "filesystem:" + this.getPath();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FileSystemResource)) {
			return false;
		}

		FileSystemResource that = (FileSystemResource) o;

		if (aliasManager != null ? !aliasManager.equals(that.aliasManager) : that.aliasManager != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return aliasManager != null ? aliasManager.hashCode() : 0;
	}
}