package com.yrkj.yrlife.been;


import com.yrkj.yrlife.widget.letterview.LetterData;

public class TestData extends Entity implements LetterData {
	private String name;
	private String num;
	private String key;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	public void setKey(String key){
		this.key = key;
	}


	@Override
	public String getSortKey() {
		return this.key;
	}

	

}
