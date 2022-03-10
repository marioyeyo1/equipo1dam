package com.campusfp.hitogrupal.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class Colors {
	//	Attributes
	private static boolean initialized = false;
	private static HashMap<EColors, String> colors =
	new HashMap<EColors, String>();
	public enum EColors {
		RESET,				//	"\u001B[0m"
		BLACK,				//	"\u001B[30m"
		RED, 				//	"\u001B[31m"
		GREEN,				//	"\u001B[32m"
		YELLOW,				//	"\u001B[33m"
		BLUE,				//	"\u001B[34m"
		PURPLE,				//	"\u001B[35m"
		CYAN,				//	"\u001B[36m"
		WHITE, 				//	"\u001B[37m"
		BLACK_BACKGROUND, 	//	"\u001B[40m"
		RED_BACKGROUND,		//	"\u001B[41m"
		GREEN_BACKGROUND,	//	"\u001B[42m"
		YELLOW_BACKGROUND,	//	"\u001B[43m"
		BLUE_BACKGROUND,	//	"\u001B[44m"
		PURPLE_BACKGROUND,	//	"\u001B[45m"
		CYAN_BACKGROUND,	//	"\u001B[46m"
		WHITE_BACKGROUND	//	"\u001B[47m"
	}
	//	Constructors
	public Colors() {
		initColorHashMap();
	}

	//	Methods
	public static void initColorHashMap() {
		colors.put(EColors.RESET, "\u001B[0m");
		colors.put(EColors.BLACK, "\u001B[30m");
		colors.put(EColors.RED, "\u001B[31m");
		colors.put(EColors.GREEN, "\u001B[32m");
		colors.put(EColors.YELLOW, "\u001B[33m");
		colors.put(EColors.BLUE, "\u001B[34m");
		colors.put(EColors.PURPLE, "\u001B[35m");
		colors.put(EColors.CYAN, "\u001B[36m");
		colors.put(EColors.WHITE, "\u001B[37m");
		colors.put(EColors.BLACK_BACKGROUND, "\u001B[40m");
		colors.put(EColors.RED_BACKGROUND, "\u001B[41m");
		colors.put(EColors.GREEN_BACKGROUND, "\u001B[42m");
		colors.put(EColors.YELLOW_BACKGROUND, "\u001B[43m");
		colors.put(EColors.BLUE_BACKGROUND, "\u001B[44m");
		colors.put(EColors.PURPLE_BACKGROUND, "\u001B[45m");
		colors.put(EColors.CYAN_BACKGROUND, "\u001B[46m");
		colors.put(EColors.WHITE_BACKGROUND, "\u001B[47m");
		initialized = true;
	}

	public int colorCount() {
		return colors.size();
	}

	public static void printChar(EColors color, char ch) {
		if (!initialized)
			initColorHashMap();
		System.out.print(colors.get(color) + ch + colors.get(EColors.RESET));
	}

	public static void printlnChar(EColors color, char ch) {
		if (!initialized)
			initColorHashMap();
		System.out.println(colors.get(color) + ch + colors.get(EColors.RESET));
	}
	
	public static void printString(EColors color, String str) {
		if (!initialized)
			initColorHashMap();
		System.out.print(colors.get(color) + str + colors.get(EColors.RESET));
	}

	public static void printlnString(EColors color, String str) {
		if (!initialized)
			initColorHashMap();
		System.out.println(colors.get(color) + str + colors.get(EColors.RESET));
	}

	public static EColors randomColor() {
		EColors e = EColors.RESET;
		Random r = new Random();

		if (!initialized)
			initColorHashMap();
		for (EColors ec : colors.keySet())
		{
			e = ec;
			if (r.nextInt() > r.nextInt())
				break;
		}
		return (e);
	}

	//	Getters & Setters
	public static Collection<EColors> getColors() {
		if (!initialized)
			initColorHashMap();
		return colors.keySet();
	}
}
