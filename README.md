Tapestry-secondary-assets-pipeline
==================================

Make a second asset pipeline, for Tapestry framework(http://tapestry.apache.org/), to load files system as assets. Use this with caution.

Configuration
==================================
Add this in your app module

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
