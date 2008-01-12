/*
 *  $Id$
 * 
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.java.javafx.svg.translator;

import java.util.Map;

/**
 * A content generator that provides the body for each of the 3 sections of
 * translator (before preprocessing, before processing, after processing).
 * In adition to the obvious fixed string implementation other feasable
 * implementations include those based on templating engines.
 *
 */
public interface ContentProvider {
	public CharSequence getContentBeforePreprocessing(Map<String, Object> vars) throws Exception;
	public CharSequence getContentBeforeProcessing(Map<String, Object> vars) throws Exception;
	public CharSequence getContentAfterProcessing(Map<String, Object> vars) throws Exception;
}
