/*******************************************************************************
 * Copyright (c) 2012 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package smalleditor.linters.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;

import smalleditor.linters.common.marker.MarkerAdapter;
import smalleditor.linters.common.marker.MarkerHandler;
import smalleditor.linters.common.problem.ProblemHandler;
import smalleditor.linters.common.text.Text;

public class CommonLinterBuilder {

	protected CommonLinter checker;

	public CommonLinterBuilder() throws CoreException {
		checker = createLinter();
	}
	
	protected CommonLinter createLinter() throws CoreException {
		return null;
	}


	public void check(IFile file) throws CoreException {
		Text code = readContent(file);
		ProblemHandler handler = new MarkerHandler(new MarkerAdapter(file),
				code);
		try {
			checker.check(code, handler);
		} catch (RuntimeException exception) {
			String message = "Failed checking file "
					+ file.getFullPath().toPortableString();
			throw new RuntimeException(message, exception);
		}
	}
	
	public void check(IDocument doc, IFile file) throws CoreException {
		Text code = new Text(doc.get());
		MarkerAdapter marker = new MarkerAdapter(file);
		marker.removeMarkers();
		ProblemHandler handler = new MarkerHandler(marker, code);
		try {
			checker.check(code, handler);
		} catch (RuntimeException exception) {
			String message = "Failed checking file";
			throw new RuntimeException(message, exception);
		}
	}

	private static Text readContent(IFile file) throws CoreException {
		try {
			InputStream inputStream = file.getContents();
			IEncodedStorage encodedStorage=((IEncodedStorage) file);
			String charset = encodedStorage.getCharset();
			return readContent(inputStream, charset);
		} catch (IOException exception) {
//			System.out.println("Failed to load resources");
		}
		return null;
	}

	private static Text readContent(InputStream inputStream, String charset)
			throws UnsupportedEncodingException, IOException {
		Text result;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, charset));
		try {
			result = new Text(reader);
		} finally {
			reader.close();
		}
		return result;
	}

}
