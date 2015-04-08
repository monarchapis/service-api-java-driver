package com.monarchapis.driver.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public interface LoadableConfiguration extends Configuration {
	public void load(byte[] content) throws IOException;

	public void load(File file) throws IOException;

	public void load(InputStream is) throws IOException;

	public void load(Reader reader) throws IOException;

	public void load(String content) throws IOException;

	public void load(URL source) throws IOException;
}
