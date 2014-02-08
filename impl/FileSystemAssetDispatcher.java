package your_package_name.tapestry.services.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.assets.AssetRequestHandler;

/**
 * 
 * @author nmeylan
 */
@UsesMappedConfiguration(AssetRequestHandler.class)
public class FileSystemAssetDispatcher implements Dispatcher {
	/**
	 * Keyed on extended path name, which includes the pathPrefix first and a
	 * trailing slash.
	 */
	private final Map<String, AssetRequestHandler> pathToHandler = CollectionFactory.newMap();

	/**
	 * List of path prefixes in the pathToHandler, sorted be descending length.
	 */
	private final List<String> assetPaths = CollectionFactory.newList();

	private final String pathPrefix;
	public FileSystemAssetDispatcher(Map<String, AssetRequestHandler> configuration,
			@Symbol(SymbolConstants.APPLICATION_VERSION) String applicationVersion,
			@Symbol(SymbolConstants.APPLICATION_FOLDER) String applicationFolder,
			@Symbol(SymbolConstants.ASSET_PATH_PREFIX) String assetPathPrefix) {
		//Replace 'fs' with a CONSTANT
		this.pathPrefix = "/" + "fs/";
		for (String path : configuration.keySet()) {
			String extendedPath = this.pathPrefix + path + "/";

			pathToHandler.put(extendedPath, configuration.get(path));

			assetPaths.add(extendedPath);
		}
		// Sort by descending length

		Collections.sort(assetPaths, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.length() - o1.length();
			}
		});
	}

	@Override
	public boolean dispatch(Request request, Response response) throws IOException {
		String path = request.getPath();
		// Remember that the request path does not include the context path, so
		// we can simply start
		// looking for the asset path prefix right off the bat.

		if (!path.startsWith(pathPrefix)) {
			return false;
		}

		for (String extendedPath : assetPaths) {
		//Replace 'fs' with a CONSTANT
			path = path.replaceAll("^\\/fs/\\w*\\/", "/fs/");
			if (path.startsWith(extendedPath)) {
				AssetRequestHandler handler = pathToHandler.get(extendedPath);

				String extraPath = path.substring(extendedPath.length());

				boolean handled = handler.handleAssetRequest(request, response, extraPath);

				if (handled) {
					return true;
				}
			}
		}

		response.sendError(HttpServletResponse.SC_NOT_FOUND, path);

		return true;
	}

}
