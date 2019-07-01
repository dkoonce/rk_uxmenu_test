/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

/**
 *
 * @author root
 */
public class Integers {

	public static int parseInt(Object obj, int backup) {
		if (obj == null) {
			return backup;
		}
		String str = obj.toString().trim();

		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			System.err.println("Expected integer, found: " + str);
			e.printStackTrace();
			return backup;
		}
	}
}
