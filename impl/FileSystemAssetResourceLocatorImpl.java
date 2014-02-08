package your_package_name.tapestry.services.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import your_package_name.tapestry.services.FileSystemAssetResourceLocator;

import org.apache.tapestry5.internal.services.ResourceDigestManager;
import org.apache.tapestry5.internal.services.ServicesMessages;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Response;

/**
 * 
 * @author nmeylan
 */
public class FileSystemAssetResourceLocatorImpl implements FileSystemAssetResourceLocator {
	private final ResourceDigestManager digestManager;

	private final Response response;

	private final AssetSource assetSource;
	private AssetFactory filesystemAssetFactory;

	public FileSystemAssetResourceLocatorImpl(ResourceDigestManager digestManager, Response response, AssetSource assetSource, FileSystemAssetFactory filesystemAssetFactory) {
		this.digestManager = digestManager;
		this.response = response;
		this.assetSource = assetSource;
		this.filesystemAssetFactory = filesystemAssetFactory;
	}

	@Override
	public Resource findClasspathResourceForPath(String path) throws IOException {
		Resource resource = this.findFilesystemResource(path);

		if (!digestManager.requiresDigest(resource)) {
			return resource;
		}

		return this.validateChecksumOfClasspathResource(resource);
		// return resource;
	}

	/**
	 * Validates the checksum encoded into the resource, and returns the true
	 * resource (with the checksum portion removed from the file name).
	 */
	private Resource validateChecksumOfClasspathResource(Resource resource) throws IOException {
		String file = resource.getFile();

		// Somehow this code got real ugly, but it's all about preventing NPEs
		// when a resource
		// that should have a digest doesn't.

		boolean valid = false;
		Resource result = resource;

		int lastdotx = file.lastIndexOf('.');

		if (lastdotx > 0) {
			int prevdotx = file.lastIndexOf('.', lastdotx - 1);

			if (prevdotx > 0) {
				String requestDigest = file.substring(prevdotx + 1, lastdotx);

				// Strip the digest out of the file name.

				String realFile = file.substring(0, prevdotx) + file.substring(lastdotx);

				result = resource.forFile(realFile);

				String actualDigest = digestManager.getDigest(result);

				valid = requestDigest.equals(actualDigest);
			}
		}

		if (valid) {
			return result;
		}

		// TODO: Perhaps we should send an exception here, so that the caller
		// can decide
		// to send the error. I'm not happy with this.

		response.sendError(HttpServletResponse.SC_FORBIDDEN, ServicesMessages.wrongAssetDigest(result));

		return null;
	}

	private Resource findFilesystemResource(String filesystemPath) {
		return filesystemAssetFactory.getRootResource().forFile(filesystemPath);
	}
}
