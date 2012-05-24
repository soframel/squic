/*******************************************************************************
 * Copyright (c) 2012 soframel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     soframel - initial API and implementation
 ******************************************************************************/
package org.soframel.android.squic.media;

public interface ISoundFilePlayer {
	public int loadFile(String resourceName);
	public void playFile(int id);
	public void playFileSynchronous(int id, Object listener);
	public void release();
}
