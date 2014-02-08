package your_package_name.tapestry.services.impl;

import java.io.IOException;

import your_package_name.tapestry.services.FileSystemAssetResourceLocator;

import org.apache.tapestry5.internal.services.ResourceStreamer;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.assets.AssetRequestHandler;

/**
 * 
 * @author nmeylan
 */
public class FileSystemAssetRequestHandler implements AssetRequestHandler {

	private final ResourceStreamer streamer;

	private final FileSystemAssetResourceLocator fileSystemAssetResourceLocator;

	private final String baseFolder;

	public FileSystemAssetRequestHandler(ResourceStreamer streamer, FileSystemAssetResourceLocator fileSystemAssetResourceLocator, String baseFolder) {
		this.streamer = streamer;
		this.fileSystemAssetResourceLocator = fileSystemAssetResourceLocator;
		this.baseFolder = baseFolder;
	}

	@Override
	public boolean handleAssetRequest(Request request, Response response, String extraPath) throws IOException {
		String assetPath = baseFolder + "/" + extraPath;

		Resource resource = fileSystemAssetResourceLocator.findClasspathResourceForPath(assetPath);

		if (resource == null) {
			return false;
		}

		streamer.streamResource(resource);

		return true;
	}

}
