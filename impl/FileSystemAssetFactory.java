package your_package_name.tapestry.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;

import your_package_name.tapestry.services.FileSystemAssetAliasManager;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.AbstractAsset;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;

/**
 * 
 * @author nmeylan
 */
public class FileSystemAssetFactory implements AssetFactory {

	private final FileSystemAssetAliasManager aliasManager;
	private final String pathPrefix;
	private FileSystemResource rootResource;

	// alias manager necessary for support of different roots
	public FileSystemAssetFactory(FileSystemAssetAliasManager aliasManager) {
		this.aliasManager = aliasManager;

		pathPrefix = "fs/";

		this.initRootResource();
	}

	private void initRootResource() {
		rootResource = new FileSystemResource(aliasManager, "/");
	}

	@Override
	public Resource getRootResource() {
		return rootResource;
	}

	@Override
	public Asset createAsset(final Resource resource) {

		return new AbstractAsset(true) {

			@Override
			public String toClientURL() {
				URL r = resource.toURL();
				//Adding MD5 to avoid browser cache issues when content change.
				String md5 = FileSystemAssetFactory.this.getFileMd5(r.toString());
				return pathPrefix + md5 + "/" + aliasManager.toClientURL(resource.getPath());
			}

			@Override
			public Resource getResource() {
				return resource;
			}
		};
	}

	public String getFileMd5(String path) {
		MessageDigest digest;
		File f;
		InputStream is;
		byte[] buffer = new byte[8192];
		int read = 0;
		try {
			digest = MessageDigest.getInstance("MD5");
			f = new File(new URI(path));
			is = new FileInputStream(f);
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}