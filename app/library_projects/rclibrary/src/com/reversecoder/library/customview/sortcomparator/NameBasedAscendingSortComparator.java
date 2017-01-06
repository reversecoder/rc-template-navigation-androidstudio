package com.reversecoder.library.customview.sortcomparator;



import java.util.Comparator;

import com.reversecoder.library.customview.model.BaseModelItem;



public class NameBasedAscendingSortComparator implements
		Comparator<BaseModelItem> {
	public int compare(BaseModelItem one, BaseModelItem another) {
		return one.getItemTitle().compareToIgnoreCase(another.getItemTitle());
		/*
		 * int returnVal = 0;
		 * 
		 * if(one.getItemTitle() < another.getItemTitle()){ returnVal = -1;
		 * }else if(one.getItemTitle() > another.getItemTitle()){ returnVal = 1;
		 * }else if(one.getItemTitle() == another.getItemTitle()){ returnVal =
		 * 0; } return returnVal;
		 */
	}
}
