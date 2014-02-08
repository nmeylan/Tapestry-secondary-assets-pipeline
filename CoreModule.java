package your_package_name.tapestry.services;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import your_package_name.tapestry.bindingprefix.ListBindingFactory;
import your_package_name.tapestry.encoders.ValueListEncoder;
import your_package_name.tapestry.services.filters.LocaleRequestFilter;
import your_package_name.tapestry.services.filters.SessionFilter;
import your_package_name.tapestry.services.impl.EscoreSessionImpl;
import your_package_name.tapestry.services.impl.FileSystemAssetAliasManagerImpl;
import your_package_name.tapestry.services.impl.FileSystemAssetBindingFactory;
import your_package_name.tapestry.services.impl.FileSystemAssetDispatcher;
import your_package_name.tapestry.services.impl.FileSystemAssetFactory;
import your_package_name.tapestry.services.impl.FileSystemAssetRequestHandler;
import your_package_name.tapestry.services.impl.FileSystemAssetResourceLocatorImpl;
import your_package_name.tapestry.services.stack.LibrariesStacks;

import org.apache.tapestry5.MetaDataConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.ResourceStreamer;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.assets.AssetRequestHandler;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.slf4j.Logger;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class CoreModule {
	/*....................................*/
	/**
	 * 
	 * @param configuration
	 */
	public static void contributeFileSystemAssetAliasManager(MappedConfiguration<String, String> configuration) {
		configuration.add("your_alias_name", "file:/" + "your_assets_directory");
	}

	public static void contributeAssetSource(MappedConfiguration<String, AssetFactory> configuration, FileSystemAssetFactory filesystemAssetFactory) {
		configuration.add("file", filesystemAssetFactory);
	}

	public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration, @InjectService("FileSystemAssetBindingFactory") BindingFactory fileBindingFactory) {
		configuration.add("file", fileBindingFactory);
	}

	public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration, @InjectService("FileSystemAssetDispatcher") Dispatcher fileSystemAssetDispatcher) {
		configuration.add("FileSystemAssetDispatcher", fileSystemAssetDispatcher, "before:ComponentEvent");
	}

	public void contributeFileSystemAssetDispatcher(MappedConfiguration<String, AssetRequestHandler> configuration, ResourceStreamer streamer, FileSystemAssetResourceLocator assetResourceLocator) {
		//Replace 'fs' with a CONSTANT
		configuration.add("your_alias_name", new FileSystemAssetRequestHandler(streamer, assetResourceLocator, "/fs/your_alias_name"));
	}

}