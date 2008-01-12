/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator.provider;

import java.util.Map;

import net.java.javafx.svg.translator.ContentProvider;


public class FixedStringContentProvider implements ContentProvider {
	private String beforeTraversing;
	private String beforeProcessing;
	private String afterProcessing;
	
	public FixedStringContentProvider() {}
	
	public FixedStringContentProvider(String beforeTraversing, String beforeProcessing, String afterProcessing) {
		this.beforeTraversing = beforeTraversing;
		this.beforeProcessing = beforeProcessing;
		this.afterProcessing = afterProcessing;
	}

	public CharSequence getContentBeforePreprocessing(Map<String, Object> vars) throws Exception {
		return beforeTraversing;
	}

	public CharSequence getContentBeforeProcessing(Map<String, Object> vars) throws Exception {
		return beforeProcessing;
	}
	
	public CharSequence getContentAfterProcessing(Map<String, Object> vars) throws Exception {
		return afterProcessing;
	}

	public String getAfterProcessing() {
		return afterProcessing;
	}

	public void setAfterProcessing(String afterProcessing) {
		this.afterProcessing = afterProcessing;
	}

	public String getBeforeProcessing() {
		return beforeProcessing;
	}

	public void setBeforeProcessing(String beforeProcessing) {
		this.beforeProcessing = beforeProcessing;
	}

	public String getBeforeTraversing() {
		return beforeTraversing;
	}

	public void setBeforeTraversing(String beforeTraversing) {
		this.beforeTraversing = beforeTraversing;
	}
}
