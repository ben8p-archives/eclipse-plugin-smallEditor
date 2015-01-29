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
package smalleditor.linters.javascript;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

import smalleditor.linters.common.CommonLinter;
import smalleditor.linters.common.CommonLinterBuilder;

public class JavascriptLinterBuilder extends CommonLinterBuilder {

	public JavascriptLinterBuilder() throws CoreException {
		super();
	}

	@Override
	protected CommonLinter createLinter() throws CoreException {
		JavascriptLinter linter = new JavascriptLinter();
		try {
			linter.load();
			//linter.configure(new Configuration());
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("Failed to intialize Linter");
		}
		return linter;
	}

}
