/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import java.util.Map;

/**
 *
 * @author root
 */
public class UXStyleFactory {
	public static UXStyle createUXStyle(){
		return createUXStyle(UXStyle.buttonStyleClasses);
	}
	
	
	public static UXStyle createUXStyle(Map<String, UXStyle> buttonClassMap){
		if(buttonClassMap != null && buttonClassMap.containsKey(UXStyle.DEFAULT_STYLE)){
			return buttonClassMap.get(UXStyle.DEFAULT_STYLE).clone();
		}
		return new UXStyle();
	}
	
	
}
