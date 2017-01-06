package com.reversecoder.library.customview.sortcomparator;

import java.util.Comparator;

import com.reversecoder.library.customview.model.BaseModelItem;

public class NameBasedDescendingSortComparator implements
		Comparator<BaseModelItem> {
	public int compare(BaseModelItem one, BaseModelItem another) {
		int returnVal = one.getItemTitle().compareToIgnoreCase(
				another.getItemTitle());

		if (returnVal < 0) {
			returnVal = 1;
		} else if (returnVal > 0) {
			returnVal = -1;
		}

		return returnVal;
	}
}
