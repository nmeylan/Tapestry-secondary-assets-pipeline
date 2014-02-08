package your_package_name.tapestry.services.impl;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AssetBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.BindingFactory;

/**
 * 
 * @author nmeylan
 */
public class FileSystemAssetBindingFactory implements BindingFactory {

	private final AssetSource source;
	private final Resource resource;

	public FileSystemAssetBindingFactory(AssetSource source, FileSystemAssetFactory factory) {
		this.source = source;
		this.resource = factory.getRootResource();
	}

	@Override
	public Binding newBinding(String description, ComponentResources container, ComponentResources component, String expression, Location location) {
		Asset asset = source.getAsset(resource, expression, null);

		return new AssetBinding(location, description, asset);
	}
}