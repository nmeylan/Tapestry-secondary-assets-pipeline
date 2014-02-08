package your_package_name.tapestry.services.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

/**
 * 
 * @author nmeylan
 */
public class AssetPathPrefixAliasMap {

	/**
	 * Map from alias to path.
	 */
	private final Map<String, String> aliasToPathPrefix = CollectionFactory.newMap();
	/**
	 * Map from path to alias.
	 */
	private final Map<String, String> pathPrefixToAlias = CollectionFactory.newMap();
	private final List<String> sortedAliases;
	private final List<String> sortedPathPrefixes;

	public AssetPathPrefixAliasMap(Map<String, String> configuration) {
		for (Map.Entry<String, String> e : configuration.entrySet()) {
			String alias = AssetPathPrefixAliasMap.withSlash(e.getKey());
			String path = AssetPathPrefixAliasMap.withSlash(e.getValue());

			aliasToPathPrefix.put(alias, path);
			pathPrefixToAlias.put(path, alias);
		}
		Comparator<String> sortDescendingByLength = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.length() - o1.length();
			}
		};

		sortedAliases = CollectionFactory.newList(aliasToPathPrefix.keySet());
		Collections.sort(sortedAliases, sortDescendingByLength);

		sortedPathPrefixes = CollectionFactory.newList(aliasToPathPrefix.values());
		Collections.sort(sortedPathPrefixes, sortDescendingByLength);
	}

	private static String withSlash(String input) {
		if (input.equals("")) {
			return input;
		}

		if (input.endsWith("/")) {
			return input;
		}

		return input + "/";
	}

	public Map<String, String> getAliasToPathPrefix() {
		return aliasToPathPrefix;
	}

	public Map<String, String> getPathPrefixToAlias() {
		return pathPrefixToAlias;
	}

	public List<String> getSortedAliases() {
		return sortedAliases;
	}

	public List<String> getSortedPathPrefixes() {
		return sortedPathPrefixes;
	}


}
