package your_package_name.tapestry.services.impl;

import java.util.Collection;
import java.util.Map;

import your_package_name.tapestry.services.FileSystemAssetAliasManager;

/**
 * 
 * @author nmeylan
 */
public class FileSystemAssetAliasManagerImpl implements FileSystemAssetAliasManager {

	private final AssetPathPrefixAliasMap pathPrefixAliasMap;

	/**
	 * @param configuration
	 *            For provision of filesystem root mappings (e.g. fs/1/xxx ->
	 *            /srv/img1/, fs/2/xxx -> /tmp/img2)
	 */
	public FileSystemAssetAliasManagerImpl(Map<String, String> configuration) {
		this.checkRootPathsContainUriFilesystemSyntax(configuration.values());
		pathPrefixAliasMap = new AssetPathPrefixAliasMap(configuration);
	}

	private void checkRootPathsContainUriFilesystemSyntax(Collection<String> rootFilepaths) {
		for (String filepath : rootFilepaths) {
			FileSystemAssetAliasManagerImpl.check(filepath.matches("^((file:///\\w)|(file:/\\w)).*?"), "Wrong filepath syntax, does not match filesystem root URI schema (e.g. file:/srv or file:///d:/tmp");
		}
	}

	/**
	 * @param resourcePath
	 *            Resource path of asset. It must start with a filesystem root
	 *            resource (defined inside configurations). It is the path xxx,
	 *            passed inside the @Asset (filesystem:xxx)
	 * @return Client url representation of resource path.
	 */
	@Override
	public String toClientURL(String resourcePath) {

		this.checkFilesystemAliasGotDefined(resourcePath);

		return resourcePath;
	}

	private void checkFilesystemAliasGotDefined(String resourcePath) {
		String filesystemAlias = this.getFilesystemAlias(resourcePath);

		FileSystemAssetAliasManagerImpl.check(filesystemAlias != null,
				String.format("Filesystem alias (first snippet of resource path) is unknown %s. This " + "was resource cannot be mapped to a filepath", resourcePath));
	}

	/**
	 *
	 */
	@Override
	public String toResourcePath(String clientURL) {
		String filesystemAlias = this.getFilesystemAlias(clientURL);

		FileSystemAssetAliasManagerImpl.check(filesystemAlias != null, String.format("Filesystem resource cannot be retrieved, because client-url %s does not contain "
				+ "a registered filesystem alias, which could be mapped to filesystem.", clientURL));

		String filesystemRoot = pathPrefixAliasMap.getAliasToPathPrefix().get(filesystemAlias);

		return clientURL.replaceFirst(filesystemAlias, filesystemRoot);
	}

	private String getFilesystemAlias(String clientURL) {
		for (String alias : pathPrefixAliasMap.getSortedAliases()) {
			if (clientURL.contains(alias)) {
				return alias;
			}
		}
		return null;
	}

	public static boolean check(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(errorMessage);
		}

		return expression;

	}
}