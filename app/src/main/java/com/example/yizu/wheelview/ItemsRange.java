/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *  
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.yizu.wheelview;

/**
 * Created by q on 2017/7/23.
 */
public class ItemsRange {
	// First item number
	private int first;
	
	// Items count
	private int count;

    public ItemsRange() {
        this(0, 0);
    }

	public ItemsRange(int first, int count) {
		this.first = first;
		this.count = count;
	}
	public int getFirst() {
		return first;
	}

	public int getLast() {
		return getFirst() + getCount() - 1;
	}
	

	public int getCount() {
		return count;
	}

	public boolean contains(int index) {
		return index >= getFirst() && index <= getLast();
	}
}