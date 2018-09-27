/*
 * Copyright (c) 2013-2014 Emeral Icemoon All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  *
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package icemoon.iceloaderext.locators;

import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.jme3.asset.AssetManager;

import icemoon.iceloader.AssetIndex;
import icemoon.iceloader.IndexItem;
import icemoon.iceloader.IndexedAssetLocator;

/**
 * Locator that locates assets on the classpath, much like
 * {@link ReflectionsClasspathLocator}, but
 * also provides an index of all classpath resources too.
 */
public class ReflectionsClasspathLocator extends com.jme3.asset.plugins.ClasspathLocator implements IndexedAssetLocator {

	private boolean loadedAssetIndex;
	private static final Logger LOG = Logger.getLogger(ReflectionsClasspathLocator.class.getName());
	private AssetIndex assetIndex;

	public ReflectionsClasspathLocator() {
	}

	public AssetIndex getIndex(AssetManager assetManager) {
		if (!loadedAssetIndex) {
			assetIndex = new AssetIndex(assetManager);
			final Collection<URL> jars = ClasspathHelper.forClassLoader(getClass().getClassLoader());
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine(String.format("Indexing from jars %s", jars));
			}
			Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(jars).setScanners(new ResourcesScanner()));
			long lastModified = 0;
			for (String s : reflections.getResources(Pattern.compile(".*"))) {
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine(String.format("    %s", s));
				}
				lastModified += s.hashCode();
				assetIndex.getBackingObject().add(new IndexItem(s, 0));
			}
			assetIndex.configure(lastModified, getClass().getSimpleName().toLowerCase() + "://" + AssetIndex.DEFAULT_RESOURCE_NAME);
			loadedAssetIndex = true;
		}
		return assetIndex;
	}
}
