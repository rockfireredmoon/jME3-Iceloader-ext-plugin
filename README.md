# Iceloader-Ext

A JME3 plugin library that further extensions Iceload with some more locators. These
ones require additional 3rd party libraries, and or not required for core features,
so are separated into a 2nd plugin.

Features include :-

* Local resources can be indexed using "reflections" library. 
* Special single-file cache that uses a Java implementation of FAT32.

## Dependencies

This plugin uses a few external libraries. The actual number needed at runtime will
depend on the usage.

* Iceloader core plugin.
* Reflections.  http://code.google.com/p/reflections/. Used for indexing classpath
  resources.  "Other" open source license. Can't find actually what though.
* Fat32Lib. https://github.com/waldheinz/fat32-lib. Used for a single-file local cache.
  LGPL.

## Usage

After installation of the plugin, a new *Library* named **Iceloader-Ext** 
will be available to add to your project. 

1. Right click your project in the Projects view, select *Properties*.
1. Click on *Libraries* in the Categories list.
1. Select the *Compile* tab if it is not already selected.
1. Click *Add Library*.
1. Locate **Iceloader-Ext**, select it, then click *Add Library*.
1. Clikc *Ok* to save and dismiss the project properties window.

That's it, your project now includes the Iceloader-Ext library.

### Single File Local Cache

A single dynamically growing local file may be used as a local asset cache. This is yet
another barrier to easy access to assets, although not a very good one, as it is actually
a virtual FAT32 file system. So given the right tools a user could access the files 
inside. However, the files inside are encrypted, and I have a use for this so it is 
included :)

To activate this, set the system property _iceloader.assetCache_ to fat32:///path/to/some/file.

### The Locators

Many of the locators can (and sometimes should) be configured. This is currently done
using some system properties. I may look for a better way at some point.

Depending on the locators you use, check the following. The defaults are unlikely to be
fine for your case.

#### icemoon.iceloaderext.locators.ReflectionsClasspathLocator 

Much the same as the standard classpath locator, but with indexing support (provided by
"reflections" library).

#### icemoon.iceloaderext.locators.ReflectionsClasspathCachingLocator 

Much the same as the ClasspathLocator, but will also allow later remote asset locators
check for the asset too. If there is one on a server, it would be used in preference.

#### icemoon.iceloaderext.locators.ReflectionsEncryptedClasspathLocator 

Much the same as the ClasspathLocator, but will assume the classpath assets are encrypted,
and decrypt them as they are returned to JME.